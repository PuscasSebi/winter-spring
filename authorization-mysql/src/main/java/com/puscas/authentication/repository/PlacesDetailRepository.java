package com.puscas.authentication.repository;

import com.puscas.authentication.model.Place;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlacesDetailRepository extends JpaRepository<Place, Integer> {
    Optional<Place> findByName(String name);
}
