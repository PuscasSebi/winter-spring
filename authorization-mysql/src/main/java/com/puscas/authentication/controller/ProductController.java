package com.puscas.authentication.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
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

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Validated
@RestController
@RequestMapping("/product")
public class ProductController {


    public static final String FILE_TO_WRITE = "newFIle.out";
    List<ProductDto> list = new ArrayList<ProductDto>() {{
        add(new ProductDto(1, "free shirt", "free-shirt", "shirts", "/images/shirt1.jpg",
                "70", "Nike", "4.5", "10", "20", "A popular shirt"));

        add(new ProductDto(2, "fit shirt", "fit-shirt", "shirts", "/images/shirt2.jpg",
                "70", "Nike", "4.5", "10", "20", "A shirt"));
        add(new ProductDto(3, "Slim shirts", "slim-shirt", "shirts", "/images/shirt3.jpg",
                "70", "Adidas", "4.5", "10", "20", "A popular shirt"));

        add(new ProductDto(4, "Golf pants", "golf-pants", "pants", "/images/pants1.jpg",
                "90", "Oliver", "4.5", "10", "20", "A popular shirt"));

        add(new ProductDto(5, "Classic pants", "classic-pants", "pants", "/images/pants2.jpg",
                "95", "Zara", "4.5", "10", "20", "Smart looking pants"));

        add(new ProductDto(6, "Fit pants", "fit-pants", "pants", "/images/pants3.jpg",
                "75", "Casely", "4.5", "10", "20", "A popular shirt"));
    }};


    String currentOrder;

    List<String> listOrders = getSsavedList();

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
    public ResponseEntity<String> getItemById(@RequestParam Integer id, @RequestHeader MultiValueMap<String, String> headers) throws JsonProcessingException {


        ProductDto item = list.stream().filter(i -> Objects.equals(i.getId(), id)).findAny().orElse(null);
        ObjectMapper objectMapper = new ObjectMapper();
        return new ResponseEntity<String>(objectMapper.writeValueAsString(item), HttpStatus.OK);
    }

    @PostMapping(value = "/placeOrder")
    public ResponseEntity<String> placeOrder(@RequestBody String map) {
        System.out.println("PLACE ORDER:   " + map);
        currentOrder = map;
        listOrders.add(map);

        listOrders = deepCloneList(listOrders);

        return new ResponseEntity<>(map, HttpStatus.OK);
    }


    @GetMapping(value = "/getOrders")
    public String getOrders() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(new ArrayList<>(listOrders));
    }

    private void serializeOrders() {


    }

    @GetMapping(value = "/orders")
    public ResponseEntity<String> getOrders(@RequestParam String id) {
        return new ResponseEntity<>(currentOrder, HttpStatus.OK);
    }


    public static List<String> deepCloneList(List<String> objectList) {
        try {
            FileOutputStream baos = new FileOutputStream(FILE_TO_WRITE);
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(objectList);
            oos.flush();
            return getSsavedList();
        } catch (EOFException eof) {
            return objectList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static List<String> getSsavedList() {
        FileInputStream in = null;
        try {
            in = new FileInputStream(FILE_TO_WRITE);
        } catch (FileNotFoundException | NullPointerException e) {
            e.printStackTrace();
        }
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(in);
        } catch (IOException| NullPointerException e) {
            e.printStackTrace();
        }
        try {
            return (List<String>) ois.readObject();
        } catch (IOException| NullPointerException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


}