package com.example.productservice.service.implementation;

import com.example.productservice.dto.PageDto;
import com.example.productservice.dto.ProductDto;
import com.example.productservice.dto.category.Category;
import com.example.productservice.entity.Product;
import com.example.productservice.exception.NotFoundException;
import com.example.productservice.feign.CategoryFeignClient;
import com.example.productservice.feign.InventoryFeignClient;
import com.example.productservice.feign.SecurityFeignClient;
import com.example.productservice.mapper.ProductMapper;
import com.example.productservice.payload.InventoryPayload;
import com.example.productservice.payload.ProductPayload;
import com.example.productservice.repository.ProductFilterRepository;
import com.example.productservice.repository.ProductRepository;
import com.example.productservice.response.ProductResponse;
import com.example.productservice.service.ProductService;
import com.example.productservice.service.cache.ProductCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper;
    private final ProductFilterRepository filterRepository;
    private final CategoryFeignClient categoryFeignClient;
    private final InventoryFeignClient inventoryFeignClient;
    private final SecurityFeignClient securityFeignClient;
    private final ProductCacheService cacheService;

    public ProductDto addProduct(ProductPayload payload, String authorizationHeader) {
        ResponseEntity<Category> categoryResponse = categoryFeignClient.getCategoryByName(payload.getCategoryName());
        Category category = categoryResponse.getBody();
        String seller = securityFeignClient.getUsername(authorizationHeader);

        if (category == null) {
            throw new NotFoundException("Category not found");
        }
        if (payload.getProductAttributes() != null) {
            payload.getProductAttributes().get(0).setCategoryName(category.getName());
            categoryFeignClient.createAttributes(payload.getProductAttributes());
        }
        if (payload.getStock().getAttributeStock() != null) {
            int totalStock = payload.getStock().getAttributeStock().values().stream()
                    .mapToInt(Integer::intValue)
                    .sum();
            payload.getStock().setTotalStock(totalStock);
        }
        if (payload.getIsFeatured() == null) {
            payload.setIsFeatured(false);
        }
        if (payload.getIsDiscounted() == null) {
            payload.setIsDiscounted(false);
            payload.setDiscountPrice(0D);
        }
        else {
            payload.setDiscountPrice(payload.getDiscountPrice());
        }

        Product product = mapper.toEntity(payload);
        product.setStatus(true);
        product.setOrderNumber(getOrderNumber() + 1);
        product.setProductCode(getProductCode() + 1);
        product.setTotalStock(payload.getStock().getTotalStock());
        product.setSellerName(seller);
        repository.save(product);

        InventoryPayload inventoryPayload = new InventoryPayload();
        inventoryPayload.setProductCode(product.getProductCode());
        inventoryPayload.setAttributeStock(payload.getStock().getAttributeStock());
        inventoryPayload.setTotalStock(product.getTotalStock());
        inventoryFeignClient.createInventory(inventoryPayload);

        return mapper.toDto(product);
    }

    public ProductDto updateProduct(Long productCode, ProductDto dto) {
        Product product = repository.findByProductCode(productCode)
                .orElseThrow(() -> new NotFoundException("Product not found!"));
        mapper.updateProductFromDto(dto, product);
        repository.save(product);
        return mapper.toDto(product);
    }

    public ProductDto getProduct(Long productCode) {
        Product product = repository.findByProductCode(productCode).
                orElseThrow(() -> new NotFoundException("Product not found!"));
        product.setRating(product.getRoundedRating());
        return mapper.toDto(product);
    }

    public ProductDto getProductCached(Long productCode) {
        ProductDto cachedDto = cacheService.getCachedProduct(productCode);
        if (cachedDto != null) {
            return cachedDto;
        }
        ProductDto productDto = getProduct(productCode);
        cacheService.cacheProduct(productCode, productDto);
        return productDto;
    }

    public PageDto<List<ProductResponse>> getAllProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Product> productPage = repository.findAllByStatusTrue(pageable);
        List<ProductResponse> productList = productPage.stream()
                .map(mapper::toResponse).toList();
        return new PageDto<>(page, size, productPage.getTotalElements(), productPage.getTotalPages(), List.of(productList));
    }

    public void deleteProduct(Long productCode) {
        Product product = repository.findByProductCode(productCode)
                .orElseThrow(() -> new NotFoundException("Product not found!"));
        product.setStatus(false);
        repository.save(product);
    }

    public void move(Long firstCode, Long secondCode) {
        Product firstProduct = repository.findByProductCode(firstCode)
                .orElseThrow(() -> new NotFoundException("Product not found!"));
        Product secondProduct = repository.findByProductCode(secondCode)
                .orElseThrow(() -> new NotFoundException("Product not found!"));
        Long firstOrderNumber = firstProduct.getOrderNumber();
        Long secondOrderNumber = secondProduct.getOrderNumber();
        firstProduct.setOrderNumber(secondOrderNumber);
        secondProduct.setOrderNumber(firstOrderNumber);
        repository.save(firstProduct);
        repository.save(secondProduct);
    }

    public PageDto<List<ProductResponse>> filterProducts(String name, String category, Double minPrice,
                                                         Double maxPrice, String colour, Double minRating, Double maxRating,
                                                         Boolean isFeatured, Boolean isDiscounted, int page, int size) {
        return filterRepository.findProductsByFilter(name, category, minPrice, maxPrice, colour, minRating, maxRating, isFeatured, isDiscounted, page, size);
    }

    public PageDto<List<ProductResponse>> getCachedFilteredProducts(String name, String category, Double minPrice,
                                                         Double maxPrice, String colour, Double minRating, Double maxRating,
                                                         Boolean isFeatured, Boolean isDiscounted, int page, int size) {
        PageDto<List<ProductResponse>> productCachedResponse = cacheService.getCachedFilteredProducts(name, category, minPrice, maxPrice, colour, minRating, maxRating, isFeatured, isDiscounted, page, size);
        if (productCachedResponse != null) {
            return productCachedResponse;
        }

        PageDto<List<ProductResponse>> productResponse = filterProducts(name, category, minPrice, maxPrice, colour, minRating, maxRating, isFeatured, isDiscounted, page, size);

        cacheService.cacheFilteredProducts(name, category, minPrice, maxPrice, colour, minRating, maxRating, isFeatured, isDiscounted, page, size);
        return productResponse;
    }

    private Long getOrderNumber() {
        Optional<Product> product = repository.findFirstByStatusTrueOrderByOrderNumberDesc();
        if (product.isEmpty()) {
            return 0L;
        }
        return product.get().getOrderNumber();
    }

    private Long getProductCode() {
        Optional<Product> product = repository.findFirstByOrderByProductCodeDesc();
        if (product.isEmpty()) {
            return 0L;
        }
        return product.get().getProductCode();
    }


}
