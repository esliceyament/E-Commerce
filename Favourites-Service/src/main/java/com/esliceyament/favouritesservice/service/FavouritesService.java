package com.esliceyament.favouritesservice.service;

import com.esliceyament.favouritesservice.dto.FavoriteProductDTO;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public interface FavouritesService {

    void addToFavourites(Long id, String authorizationHeader);
    Set<FavoriteProductDTO> getFavourites(String authorizationHeader);
    void deleteFromFavourites(Long id, String authorizationHeader);
    void clearFavourites(String authorizationHeader);
}
