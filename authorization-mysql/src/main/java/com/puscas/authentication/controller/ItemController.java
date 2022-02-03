package com.puscas.authentication.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.puscas.authentication.controller.model.ItemDTO;
import io.micrometer.core.annotation.Timed;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Validated
@RestController
@RequestMapping("/item")
public class ItemController {

    List<ItemDTO> list = new ArrayList<ItemDTO>(){{
        add(new ItemDTO(12L,"name1","s","sale"));
        add(new ItemDTO(13L,"name2","s","sale"));
        add(new ItemDTO(14L,"name3","c","sale"));
        add(new ItemDTO(15L,"name4","a","sale"));
        add(new ItemDTO(15L,"name5","b","sale"));
    }};

    @Autowired
    ObjectMapper objectMapper;

    @GetMapping(value = "/find", produces= MediaType.APPLICATION_JSON_VALUE)
    @Timed(value = "item.service.get.all.timer")
    @ResponseBody()
    public List<ItemDTO> getAllItems() {

        return new ArrayList<>(list);
    }

    @GetMapping(value = "/find/by-id")
    public ResponseEntity<ItemDTO> getItemById(@RequestParam Long id, @RequestHeader MultiValueMap<String, String> headers) {

        ItemDTO item = new ItemDTO();

        item = list.stream().filter(i -> i.getItemId() == id).findAny().orElse(null);

        return new ResponseEntity<ItemDTO>(item, HttpStatus.OK);
    }

}
