package com.puscas.authentication;

import com.puscas.authentication.controller.PlaceController;
import com.puscas.authentication.model.Place;
import com.puscas.authentication.repository.PlaceRepositoryImpl;
import org.junit.gen5.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PlaceController.class)
public class PlaceControllerTest {
    @MockBean
    private PlaceRepositoryImpl placeRepository;

    @Autowired
    private MockMvc mockMvc;

    private String creator = "CreatorName";

    private List<Place> places = new ArrayList<>(){{
       add(new PlacesDetailsRepositoryTest().p);
       add(new PlacesDetailsRepositoryTest().p2);
    }};
    Page<Place> pageDomain = new PageImpl<>(places, PageRequest.
            of(1,2, Sort.by(Sort.Order.asc("name"))), 0);
    @Test
    void evaluatesPageableParameter() throws Exception {
        when(placeRepository.findByCreator(anyString(), any(Pageable.class))).thenReturn(pageDomain);
        final MvcResult mvcResult = mockMvc.perform(get("/ByCreator/pagination")
                        .param("page", "5")
                        .param("size", "10")
                        .param("creator", creator)
                        .param("sort", "id,desc") // <-- no space after comma!!!
                        .param("sort", "name,asc")
                        .with(user("admin").password("pass").roles("ADMIN"))
                ) // <-- no space after comma!!!
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..content[0].id").value(1))
                .andExpect(jsonPath("$..content[0].name").value("Aname"))
                .andReturn();


        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(placeRepository).findByCreator(eq(creator), pageableCaptor.capture());
        PageRequest pageable = (PageRequest) pageableCaptor.getValue();
        Assertions.assertEquals(pageable.getPageNumber(), 5);
        Assertions.assertEquals(pageable.getPageSize(), 10);
        final List<Sort.Order> collect = pageable.getSort().get().collect(Collectors.toList());
        Assertions.assertEquals(collect.get(0).getDirection(), Sort.Direction.DESC);
        Assertions.assertEquals(collect.get(0).getProperty(), "id");

        Assertions.assertEquals(collect.get(1).getDirection(), Sort.Direction.ASC);
        Assertions.assertEquals(collect.get(1).getProperty(), "name");
    }

    @Test
    void evaluatesSliceParameter() throws Exception {
        when(placeRepository.findByCreator(any(Pageable.class), anyString())).thenReturn(pageDomain);
        final MvcResult mvcResult = mockMvc.perform(get("/byCreator/slice")
                        .param("page", "5")
                        .param("size", "10")
                        .param("creator", creator)
                        .param("sort", "id,desc") // <-- no space after comma!!!
                        .param("sort", "name,asc")
                        .with(user("admin").password("pass").roles("ADMIN"))
                ) // <-- no space after comma!!!
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..content[0].id").value(1))
                .andExpect(jsonPath("$..content[0].name").value("Aname"))
                .andReturn();


        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(placeRepository).findByCreator(pageableCaptor.capture(), eq(creator));
        PageRequest pageable = (PageRequest) pageableCaptor.getValue();
        Assertions.assertEquals(pageable.getPageNumber(), 5);
        Assertions.assertEquals(pageable.getPageSize(), 10);
        final List<Sort.Order> collect = pageable.getSort().get().collect(Collectors.toList());
        Assertions.assertEquals(collect.get(0).getDirection(), Sort.Direction.DESC);
        Assertions.assertEquals(collect.get(0).getProperty(), "id");

        Assertions.assertEquals(collect.get(1).getDirection(), Sort.Direction.ASC);
        Assertions.assertEquals(collect.get(1).getProperty(), "name");

    }
}
