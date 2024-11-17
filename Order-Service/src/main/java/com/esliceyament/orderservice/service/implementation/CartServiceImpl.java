package com.esliceyament.orderservice.service.implementation;

import com.esliceyament.orderservice.dto.Product;
import com.esliceyament.orderservice.entity.Cart;
import com.esliceyament.orderservice.entity.CartItem;
import com.esliceyament.orderservice.entity.DiscountCode;
import com.esliceyament.orderservice.enums.DiscountType;
import com.esliceyament.orderservice.enums.OrderStatus;
import com.esliceyament.orderservice.exception.DiscountNotActiveException;
import com.esliceyament.orderservice.exception.NotFoundException;
import com.esliceyament.orderservice.exception.QuantityOutOfStockException;
import com.esliceyament.orderservice.exception.UsedDiscountException;
import com.esliceyament.orderservice.feign.InventoryFeignClient;
import com.esliceyament.orderservice.feign.ProductFeignClient;
import com.esliceyament.orderservice.feign.SecurityFeignClient;
import com.esliceyament.orderservice.mapper.CartItemMapper;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ItemRepository itemRepository;
    private final SecurityFeignClient securityFeignClient;
    private final ProductFeignClient productFeignClient;
    private final InventoryFeignClient inventoryFeignClient;
    private final DiscountCodeRepository discountCodeRepository;
    private final CartItemMapper mapper;

    public void addItemToCart(CartItemPayload payload, String authorizationHeader) {
        ResponseEntity<Product> productResponse = productFeignClient.getProduct(payload.getProductCode());
        Product product = productResponse.getBody();
        if (product == null) {
            throw new NotFoundException("Product not found");
        }

        Cart cart = findCart(authorizationHeader);

        Optional<CartItem> cartItemOpt = cart.getCartItems().stream()
                .filter(item -> item.getProductCode().equals(payload.getProductCode()))
                .findFirst();
        if (cartItemOpt.isPresent()) {
            CartItem cartItem = cartItemOpt.get();
            cartItem.setQuantity(updateByOneItemQuantity(payload.getProductCode(), cartItem));
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setProductCode(payload.getProductCode());
            cartItem.setProductName(product.getName());
            cartItem.setSellerName(product.getSellerName());
            cartItem.setQuantity(1);
            cartItem.setSelectedAttributes(payload.getSelectedAttributes());
            cartItem.setPricePerUnit(product.getPrice());
            cartItem.setAddedAt(LocalDateTime.now());
            cartItem.setIsRemoved(false);
            cartItem.setStatus(OrderStatus.PENDING);
            cartItem.setStageStatus(0);
            cartItem.setCart(cart);

            cart.getCartItems().add(cartItem);
        }

        cart.setTotalPrice(getTotalPrice(cart.getCartItems()));
        cart.setCreatedAt(LocalDateTime.now());
        if (cart.getDiscountCode() != null) {
            cart.setDiscountPrice(calculateDiscountPrice(cart.getDiscountCode(), cart));
        }
        cartRepository.save(cart);
    }

    public CartResponse getCardResponse(String authorizationHeader) {
        Cart cart = findCart(authorizationHeader);
        CartResponse response = new CartResponse();
        response.setCartItems(cart.getCartItems().stream()
                .map(mapper::toResponse).collect(Collectors.toSet()));
        response.setTotalPrice(cart.getTotalPrice());
        response.setDiscountCode(cart.getDiscountCode());
        response.setDiscountPrice(cart.getDiscountPrice());
        return response;
    }

    public void deleteItemFromCart(Long productCode, String authorizationHeader) {
        Cart cart = findCart(authorizationHeader);
        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProductCode().equals(productCode) && !item.getIsRemoved())
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Item not found"));

        cartItem.setIsRemoved(true);
        cartItem.setCart(null);

        cart.getCartItems().remove(cartItem);
        cart.setTotalPrice(cart.getTotalPrice() - removedPrice(cartItem));
        if (cart.getDiscountCode() != null) {
            cart.setDiscountPrice(calculateDiscountPrice(cart.getDiscountCode(), cart));
        }
        cartRepository.save(cart);
    }

    public int updateItemQuantity(Long productCode, int quantity, String authorizationHeader) {
        Cart cart = findCart(authorizationHeader);
        CartItem cartItem = cart.getCartItems().stream()
                        .filter(item -> item.getProductCode().equals(productCode))
                                .findFirst()
                                        .orElseThrow(() -> new NotFoundException("Product with code " + productCode + " not found in cart."));

        int stock = inventoryFeignClient.getStock(productCode, cartItem.getSelectedAttributes());
        if (quantity > stock || cartItem.getQuantity() <= 0) {
            throw new QuantityOutOfStockException("You cannot choose " + quantity + " items!");
        }

        cartItem.setQuantity(quantity);

        itemRepository.save(cartItem);
        cart.setTotalPrice(getTotalPrice(cart.getCartItems()));
        if (cart.getDiscountCode() != null) {
            cart.setDiscountPrice(calculateDiscountPrice(cart.getDiscountCode(), cart));
        }
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
            cart.setDiscountPrice(0D);
            cart.getCartItems().clear();
        }

        cartRepository.save(cart);
    }

    public Double useDiscountCode(String code, String authorizationCode) {
        Cart cart = findCart(authorizationCode);

        if (code.isBlank() || code.isEmpty()) {
            cart.setDiscountCode(null);
            cart.setDiscountPrice(null);
            return null;
        }

        DiscountCode discount = discountCodeRepository.findByCode(code)
                .orElseThrow(() -> new NotFoundException("No such discount!"));

        if (!discount.isActive()) {
            throw new DiscountNotActiveException("This discount is not active!");
        }

        if (cart.getDiscountCodes().contains(discount.getCode())) {
            throw new UsedDiscountException("This code was used");
        }

        double discountPrice = calculateDiscountPrice(code, cart);

        cart.setDiscountPrice(Math.max(discountPrice, 0));
        cart.setDiscountCode(code);

        cartRepository.save(cart);

        return cart.getDiscountPrice();
    }

    private int updateByOneItemQuantity(Long productCode, CartItem cartItem) {
        int stock = inventoryFeignClient.getStock(productCode, cartItem.getSelectedAttributes());
        int quantity = cartItem.getQuantity() + 1;
        if (quantity > stock) {
            throw new QuantityOutOfStockException("Maximum quantity is " + (quantity - 1));
        }
        return quantity;
    }

    private Double calculateDiscountPrice(String code, Cart cart) {
        DiscountCode discount = discountCodeRepository.findByCode(code)
                .orElseThrow(() -> new NotFoundException("Not found such discount!"));
        if (discount.getDiscountType().equals(DiscountType.AMOUNT)) {
            return cart.getTotalPrice() - discount.getDiscount();
        } else {
            return cart.getTotalPrice() * (100 - discount.getDiscount()) / 100;
        }
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
