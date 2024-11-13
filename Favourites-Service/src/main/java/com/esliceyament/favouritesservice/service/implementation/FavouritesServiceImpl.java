package com.esliceyament.favouritesservice.service.implementation;

import com.esliceyament.favouritesservice.dto.FavoriteProductDTO;
import com.esliceyament.favouritesservice.dto.ProductDto;
import com.esliceyament.favouritesservice.entity.Favourites;
import com.esliceyament.favouritesservice.feign.ProductFeignClient;
import com.esliceyament.favouritesservice.feign.SecurityFeignClient;
import com.esliceyament.favouritesservice.mapper.ProductMapper;
import com.esliceyament.favouritesservice.repository.FavouritesRepository;
import com.esliceyament.favouritesservice.service.FavouritesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavouritesServiceImpl implements FavouritesService {
    private final FavouritesRepository repository;
    private final ProductFeignClient productFeignClient;
    private final SecurityFeignClient securityFeignClient;
    private final ProductMapper mapper;


    @Override
    public void addToFavourites(Long productCode, String authorizationHeader) {
        ProductDto productDTO = productFeignClient.getProductCached(productCode).getBody();
        System.out.println(productDTO);
        if (productDTO == null) {
            throw new RuntimeException("Product not found");
        }
        String username = securityFeignClient.getUsername(authorizationHeader);

        FavoriteProductDTO favoriteProductDTO = new FavoriteProductDTO();
        favoriteProductDTO.setProductCode(productCode);
        favoriteProductDTO.setName(productDTO.getName());
        favoriteProductDTO.setSellerName(productDTO.getSellerName());
        favoriteProductDTO.setPrice(productDTO.getPrice());
        favoriteProductDTO.setIsDiscounted(productDTO.getIsDiscounted());
        favoriteProductDTO.setDiscountPrice(productDTO.getDiscountPrice());
        favoriteProductDTO.setImageUrls(productDTO.getImageUrls());

        Favourites favourites = repository.findByUsername(username)
                        .orElseGet(() -> createNewFavourite(username));
        favourites.getProductIds().add(productDTO.getProductCode());
        repository.save(favourites);
    }
///imageurl v produkte list a zdes string rewit, rewit eto i v ordere i v carte
    @Override
    public void deleteFromFavourites(Long id, String authorizationHeader) {
        String username = securityFeignClient.getUsername(authorizationHeader);
        Favourites favourites = repository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Not found"));
        Long favouriteItem = favourites.getProductIds().stream().filter(item -> item.equals(id))
                .findFirst().orElseThrow();
        favourites.getProductIds().remove(favouriteItem);
        repository.save(favourites);
    }

    @Override
    public void clearFavourites(String authorizationHeader) {
        String username = securityFeignClient.getUsername(authorizationHeader);
        Favourites favourites = repository.findByUsername(username)
                .orElseThrow();
        favourites.getProductIds().clear();
        repository.save(favourites);
    }

    @Override
    public Set<FavoriteProductDTO> getFavourites(String authorizationHeader) {
        String username = securityFeignClient.getUsername(authorizationHeader);
        Favourites favourites = repository.findByUsername(username)
                .orElse(null);
        if (favourites == null || favourites.getProductIds().isEmpty()) {
            return Collections.emptySet();
        }
        Set<ProductDto> productDto = productFeignClient.getProductsById(favourites.getProductIds()).getBody();
        if (productDto == null) {
            return Collections.emptySet();
        }
        return productDto.stream()
                .map(mapper::productDtoToFavoriteProductDTO).collect(Collectors.toSet());
    }

    private Favourites createNewFavourite(String username) {
        Favourites favourites = new Favourites();
        favourites.setUsername(username);
        return repository.save(favourites);
    }
}
