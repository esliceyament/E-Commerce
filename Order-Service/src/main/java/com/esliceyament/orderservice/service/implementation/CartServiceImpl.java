package com.esliceyament.orderservice.service.implementation;

import com.esliceyament.orderservice.dto.Product;
import com.esliceyament.orderservice.entity.Cart;
import com.esliceyament.orderservice.entity.CartItem;
import com.esliceyament.orderservice.entity.DiscountCode;
import com.esliceyament.orderservice.feign.InventoryFeignClient;
import com.esliceyament.orderservice.feign.ProductFeignClient;
import com.esliceyament.orderservice.feign.SecurityFeignClient;
import com.esliceyament.orderservice.payload.CartItemPayload;
import com.esliceyament.orderservice.repository.CartRepository;
import com.esliceyament.orderservice.repository.DiscountCodeRepository;
import com.esliceyament.orderservice.repository.ItemRepository;
import com.esliceyament.orderservice.response.CartResponse;
import com.esliceyament.orderservice.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ItemRepository itemRepository;
    private final SecurityFeignClient securityFeignClient;
    private final ProductFeignClient productFeignClient;
    private final InventoryFeignClient inventoryFeignClient;
    private final DiscountCodeRepository discountCodeRepository;

    public void addItemToCart(CartItemPayload payload, String authorizationHeader) {
        ResponseEntity<Product> productResponse = productFeignClient.getProduct(payload.getProductCode());
        Product product = productResponse.getBody();
        if (product == null) {
            throw new RuntimeException("Product not found");
        }

        Cart cart = findCart(authorizationHeader);

        Optional<CartItem> cartItemOpt = cart.getCartItems().stream()
                .filter(item -> item.getProductCode().equals(payload.getProductCode()))
                .findFirst();
        if (cartItemOpt.isPresent()) {
            CartItem cartItem = cartItemOpt.get();
            cartItem.setQuantity(cartItem.getQuantity() + 1);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setProductCode(payload.getProductCode());
            cartItem.setQuantity(1);
            cartItem.setSelectedAttributes(payload.getSelectedAttributes());
            cartItem.setProductName(product.getName());
            cartItem.setPricePerUnit(product.getPrice());
            cartItem.setAddedAt(LocalDateTime.now());
            cartItem.setIsRemoved(false);
            cartItem.setCart(cart);

            cart.getCartItems().add(cartItem);
        }

        cart.setTotalPrice(getTotalPrice(cart.getCartItems()));
        cart.setCreatedAt(LocalDateTime.now());

        cartRepository.save(cart);

    }

    public CartResponse getCardResponse(String authorizationHeader) {
        Cart cart = findCart(authorizationHeader);
        CartResponse response = new CartResponse();
        response.setCartItems(cart.getCartItems());
        response.setTotalPrice(cart.getTotalPrice());
        return response;
    }

    public void deleteItemFromCart(Long productCode, String authorizationHeader) {
        Cart cart = findCart(authorizationHeader);
        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProductCode().equals(productCode) && !item.getIsRemoved())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item not found"));

        cartItem.setIsRemoved(true);
        cartItem.setCart(null);

        cart.setTotalPrice(cart.getTotalPrice() - removedPrice(cartItem));
        cartRepository.save(cart);
    }

    public int updateItemQuantity(Long productCode, int quantity, String authorizationHeader) {

        Cart cart = findCart(authorizationHeader);
        CartItem cartItem = cart.getCartItems().stream()
                        .filter(item -> item.getProductCode().equals(productCode))
                                .findFirst()
                                        .orElseThrow(() -> new RuntimeException("Product with code " + productCode + " not found in cart."));

        int stock = inventoryFeignClient.getStock(productCode, cartItem.getSelectedAttributes());
        System.out.println(stock);
        if (quantity > stock || cartItem.getQuantity() <= 0) {
            throw new RuntimeException("Choose appropriate quantity");
        }

        cartItem.setQuantity(quantity);

        itemRepository.save(cartItem);
        cart.setTotalPrice(getTotalPrice(cart.getCartItems()));
        cartRepository.save(cart);
        return quantity;
    }

    public void clearCart(String authorizationHeader) {
        Cart cart = findCart(authorizationHeader);
        if (!cart.getCartItems().isEmpty()) {
            cart.getCartItems().stream().forEach(item -> {
                item.setIsRemoved(true);
                item.setCart(null);
            });
            cart.setTotalPrice(0D);
        }

        cartRepository.save(cart);
    }

    public Double useDiscountCode(String code, String authorizationCode) {
        Cart cart = findCart(authorizationCode);
        DiscountCode discount = discountCodeRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("NotFound"));

        if (!discount.isActive()) {
            throw new RuntimeException("Is not active");
        }

        if (cart.getDiscountCodes().contains(discount.getCode())) {
            throw new RuntimeException("This code was used");
        }

        double discountPrice;
        if (discount.getDiscountType().toString().equals("PERCENTAGE")) {
            discountPrice = cart.getTotalPrice() * (100 - discount.getDiscount()) / 100;
        } else {
            discountPrice = cart.getTotalPrice() - discount.getDiscount();
        }

        cart.setDiscountPrice(Math.max(discountPrice, 0));
        cart.setDiscountCode(code);

        cartRepository.save(cart);
        return cart.getDiscountPrice();
    }

    private Double removedPrice(CartItem cartItem) {
        return cartItem.getPricePerUnit() * cartItem.getQuantity();
    }
    private Cart createNewCart(String buyerName) {
        Cart cart = new Cart();
        cart.setBuyerName(buyerName);
        cart.setIsActive(true);
        cart.setTotalPrice(0D);
        return cartRepository.save(cart);
    }

    private Double getTotalPrice(Set<CartItem> cartItemList) {
        return cartItemList.stream().mapToDouble(x -> x.getPricePerUnit() * x.getQuantity()).sum();
    }

    private Cart findCart(String authorizationHeader) {
        String username = securityFeignClient.getUsername(authorizationHeader);
        return cartRepository.findActiveCartByBuyerName(username)
                .orElseGet(() -> createNewCart(username));
    }


}
