package com.esliceyament.favouritesservice.controller;

import com.esliceyament.favouritesservice.dto.FavoriteProductDTO;
import com.esliceyament.favouritesservice.service.FavouritesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/favourites")
@RequiredArgsConstructor
public class FavouritesController {

    private final FavouritesService favouritesService;


    @PostMapping("/{id}")
    public ResponseEntity<?> addToFavourites(@PathVariable Long id, @RequestHeader("Authorization") String authorizationHeader) {
        favouritesService.addToFavourites(id, authorizationHeader);
        return ResponseEntity.ok("Item added!");
    }


    @GetMapping
    public ResponseEntity<Set<FavoriteProductDTO>> getFavourites(@RequestHeader("Authorization") String authorizationHeader) {
        Set<FavoriteProductDTO> favourites = favouritesService.getFavourites(authorizationHeader);
        return ResponseEntity.ok(favourites);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> deleteFromFavourites(@PathVariable Long id, @RequestHeader("Authorization") String authorizationHeader) {
        favouritesService.deleteFromFavourites(id, authorizationHeader);
        return ResponseEntity.ok("Item deleted!");
    }

    @PutMapping("/clear")
    public ResponseEntity<?> clearFavourites(@RequestHeader("Authorization") String authorizationHeader) {
        favouritesService.clearFavourites(authorizationHeader);
        return ResponseEntity.ok("Favourites cleared!");
    }
}
