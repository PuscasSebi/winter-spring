package com.puscas.authentication;

import com.puscas.authentication.config.TestConfiguration;
import com.puscas.authentication.model.Place;
import com.puscas.authentication.repository.PlaceRepositoryImpl;
import org.junit.gen5.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.List;
import java.util.Optional;


@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)

public class PlacesDetailsRepositoryTest {


    final public static String placeCreator = "Puscasa";
    final String description = "description";
    final String placeImage = "image/url";
    final String name = "Aname";
    final String metadata = "[\"isPrimi\"]";
    public Place p = new Place(1, placeCreator, name, description, placeImage, metadata);
    public Place p2 = new Place(2, placeCreator, "BSortAfterName", "cSortBeforeDescription"
            , placeImage, metadata);

    @Autowired
    private PlaceRepositoryImpl placesDetailRepository;

    @AfterAll
    public void truncateTable(){
        placesDetailRepository.deleteAll();
    }

    @BeforeAll
    public void createData(){
        placesDetailRepository.save(p);
        placesDetailRepository.save(p2);
    }

    @Test
    public void testSave(){
        Optional<Place> byId = placesDetailRepository.findById(1);
        assertInsertedDataFound(byId);
    }
    @Test
    public void testGetByName(){
        final Optional<Place> byName = placesDetailRepository.findByName(name);
        final Optional<Place> byDescription = placesDetailRepository.findByDescription(name);
       Assertions.assertFalse(byDescription.isPresent());
        assertInsertedDataFound(byName);
    }

    @Test
    public void testGetByDescription(){
        final Optional<Place> byDescription = placesDetailRepository.findByDescription(description);
        final Optional<Place> byName = placesDetailRepository.findByDescription(name);
        Assertions.assertFalse(byName.isPresent());
        assertInsertedDataFound(byDescription);
    }

    @Test
    public void testSortByNameFindByPlaceholder(){
        final List<Place> byCreatorOrderByNameDesc = placesDetailRepository.findByCreatorOrderByNameDesc(placeCreator);
        Assertions.assertEquals(byCreatorOrderByNameDesc.get(0).getId(), 2);
        Assertions.assertEquals(byCreatorOrderByNameDesc.get(1).getId(), 1);
    }
    @Test
    public void testSortByNameFindByPlaceholderV2(){
        final List<Place> byCreatorOrderByNameDesc = placesDetailRepository.findByCreator(placeCreator, Sort.by(Sort.Order.desc("name")));
        Assertions.assertEquals(byCreatorOrderByNameDesc.get(0).getId(), 2);
        Assertions.assertEquals(byCreatorOrderByNameDesc.get(1).getId(), 1);
    }

    @Test
    public void testSortByNameFindByPlaceholderPagination(){
        final Page<Place> all = placesDetailRepository.findAll(PageRequest.of(1, 1));
        Assertions.assertEquals(all.getContent().get(0).getId(), 2);
        Page<Place> byCreatorOrderByNameDesc = placesDetailRepository.findByCreator(placeCreator,
                PageRequest.of(0,1, Sort.by("name").descending()));
        Assertions.assertEquals(byCreatorOrderByNameDesc.getContent().get(0).getId(), 2);
        byCreatorOrderByNameDesc = placesDetailRepository.findByCreator(placeCreator,
                PageRequest.of(1,1, Sort.by(Sort.Direction.DESC, "name")));
        Assertions.assertEquals(byCreatorOrderByNameDesc.getContent().get(0).getId(), 1);
    }


    private void assertInsertedDataFound(Optional<Place> optionalPlace) {
        Assertions.assertTrue(optionalPlace.isPresent());
        final Place place = optionalPlace.get();
        Assertions.assertNotNull(place);
        Assertions.assertEquals(place.getCreator(),placeCreator);
        Assertions.assertEquals(place.getDescription(),description);
        Assertions.assertEquals(place.getPlaceImage(),placeImage);
        Assertions.assertEquals(place.getName(),name);
        Assertions.assertEquals(place.getMetadata(),metadata);
    }
}
