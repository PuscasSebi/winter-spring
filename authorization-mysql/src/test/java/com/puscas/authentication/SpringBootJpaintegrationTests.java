package com.puscas.authentication;

import com.puscas.authentication.config.TestConfiguration;
import com.puscas.authentication.model.Place;
import com.puscas.authentication.repository.PlaceRepositoryImpl;
import com.puscas.authentication.repository.RoleDetailRepository;
import com.puscas.authentication.repository.UserDetailRepository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static com.puscas.authentication.PlacesDetailsRepositoryTest.placeCreator;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AuthenticationApplication.class, webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = TestConfiguration.class)

public class SpringBootJpaintegrationTests {

    @Autowired
    private PlaceRepositoryImpl placeRepository;


    @Autowired
    private TestRestTemplate restTemplate;
    //only available when configuring with webEnvironment

    @BeforeAll
    public void createData(){

    }
    @Test
    public void saveAndRelease(){
        placeRepository.save(new Place(1,"creater","name","description","image","metadata"));

        final Optional<Place> byName = placeRepository.findByName("name");
        assertTrue(byName.isPresent());
    }

    @Test
    @WithMockUser(username = "user1", password = "pwd", roles = "USER")
    public void retrievePlaces(){
        placeRepository.save(new PlacesDetailsRepositoryTest().p);
        placeRepository.save(new PlacesDetailsRepositoryTest().p2);
        final HashMap<String, String> urlVariables = new HashMap<>() {{
            put("creator",placeCreator);
        }};
        final ResponseEntity<String> forObject = this.restTemplate.getForEntity("/ByCreator/pagination?creator={creator}", String.class, urlVariables);

        assertTrue(forObject.getBody().contains("20"));
        System.out.println(forObject);
    }


}
