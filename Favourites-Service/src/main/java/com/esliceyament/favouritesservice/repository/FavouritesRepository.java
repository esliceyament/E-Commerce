package com.esliceyament.favouritesservice.repository;

import com.esliceyament.favouritesservice.entity.Favourites;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FavouritesRepository extends JpaRepository<Favourites, Long> {
    Optional<Favourites> findByUsername(String username);
}
