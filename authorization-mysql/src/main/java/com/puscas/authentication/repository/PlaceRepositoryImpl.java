package com.puscas.authentication.repository;

import com.puscas.authentication.model.Place;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface PlaceRepositoryImpl extends PagingAndSortingRepository<Place, Integer> {
    Optional<Place> findByName(String name);
    Optional<Place> findByDescription(String description);

    List<Place> findByCreatorOrderByNameDesc(String creator);

    List<Place> findByCreator(String creator, Sort sort);

    Page<Place> findByCreator(String creator, Pageable pageable);

    Slice<Place> findByCreator( Pageable pageable, String creator);
}
