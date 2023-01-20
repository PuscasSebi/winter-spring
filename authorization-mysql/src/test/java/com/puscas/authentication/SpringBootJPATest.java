package com.puscas.authentication;

import com.puscas.authentication.model.Place;
import com.puscas.authentication.repository.PlacesDetailRepository;
import org.junit.gen5.api.Assertions;
import org.junit.jupiter.api.Test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;


@DataJpaTest
public class SpringBootJPATest {

    @Autowired
    private PlacesDetailRepository placesDetailRepository;

    @Test
    public void testSave(){
        Place p = new Place(1, "Puscasa","name", "description","image/url");
        placesDetailRepository.save(p);

        Place byId = placesDetailRepository.getById(1);
        Assertions.assertNotNull(byId);
    }
}
