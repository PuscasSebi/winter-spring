package com.puscas.authentication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.puscas.authentication.controller.model.ProductDto;
import io.micrometer.core.annotation.Timed;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Validated
@RestController
@RequestMapping("/product")
public class ProductController {


    List<ProductDto> list = new ArrayList<ProductDto>() {{
        add(new ProductDto(1, "free shirt", "free-shirt", "shirts", "/images/shirt1.jpg",
                "70", "Nike", "4.5", "10", "20", "A popular shirt"));

        add(new ProductDto(2, "fit shirt", "fit-shirt", "shirts", "/images/shirt2.jpg",
                "70", "Nike", "4.5", "10", "20", "A shirt"));
        add(new ProductDto(3, "Slim shirts", "slim-shirt", "shirts", "/images/shirt3.jpg",
                "70", "Adidas", "4.5", "10", "20", "A popular shirt"));

        add(new ProductDto(4, "Golf pants", "golf-pants", "pants", "/images/pants.jpg",
                "90", "Oliver", "4.5", "10", "20", "A popular shirt"));


        add(new ProductDto(5, "free shirt", "free-shirt", "shirts", "/images/pants1.jpg",
                "95", "Zara", "4.5", "10", "20", "Smart looking pants"));

        add(new ProductDto(6, "Fit pants", "fit-pants", "pants", "/images/pants2.jpg",
                "75", "Casely", "4.5", "10", "20", "A popular shirt"));
    }};


    @Autowired
    ObjectMapper objectMapper;

    @SneakyThrows
    @GetMapping(value = "/find", produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed(value = "item.service.get.all.timer")
    @ResponseBody()
    public String getAllItems() {

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(new ArrayList<>(list));
    }

    @GetMapping(value = "/find/by-id")
    public ResponseEntity<ProductDto> getItemById(@RequestParam Integer id, @RequestHeader MultiValueMap<String, String> headers) {


        ProductDto item = list.stream().filter(i -> Objects.equals(i.getId(), id)).findAny().orElse(null);

        return new ResponseEntity<ProductDto>(item, HttpStatus.OK);
    }
}
