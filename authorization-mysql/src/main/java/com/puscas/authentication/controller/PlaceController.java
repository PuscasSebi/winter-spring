package com.puscas.authentication.controller;

import com.puscas.authentication.model.Place;
import com.puscas.authentication.repository.PlaceRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceRepositoryImpl placeRepository;

    @GetMapping(path = "/ByCreator/pagination")
    public Page<Place> loadPlacesByName(
            @RequestParam String creator,
            @PageableDefault(page = 0, size = 20)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "name", direction = Sort.Direction.DESC),
                    @SortDefault(sort = "id", direction = Sort.Direction.ASC)
            })
            Pageable pageable
            ){
        return placeRepository.findByCreator(creator, pageable);
    }

    @GetMapping(path = "/byCreator/slice")
    public Slice<Place> loadByCreatorSlice( @RequestParam String creator,
                                     @PageableDefault(page = 0, size = 20)
                                     @SortDefault.SortDefaults({
                                             @SortDefault(sort = "name", direction = Sort.Direction.DESC),
                                             @SortDefault(sort = "id", direction = Sort.Direction.ASC)
                                     })
                                     Pageable pageable){
        return placeRepository.findByCreatorSlice(creator, pageable);
    }

}
