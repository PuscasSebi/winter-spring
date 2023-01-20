package com.puscas.authentication.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.puscas.authentication.controller.model.ProductDto;
import com.puscas.authentication.controller.model.SalesData;
import com.puscas.authentication.controller.model.Summary;
import com.puscas.authentication.controller.model.UserInfo;
import io.micrometer.core.annotation.Timed;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import javax.websocket.server.PathParam;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping("/product")
public class ProductController {


    public static final String FILE_TO_WRITE = "efwfew.out";
    List<ProductDto> productDtoList = new ArrayList<ProductDto>() {{
        add(new ProductDto(1, "free shirt", "free-shirt", "shirts", "/images/shirt1.jpg",
                "70", "Nike", "4.5", "10", "20", "A popular shirt", true, "/images/banner1.jpg"));

        add(new ProductDto(2, "fit shirt", "fit-shirt", "shirts", "/images/shirt2.jpg",
                "70", "Nike", "4.5", "10", "20", "A shirt", true, "/images/banner2.jpg"));
        add(new ProductDto(3, "Slim shirts", "slim-shirt", "shirts", "/images/shirt3.jpg",
                "70", "Adidas", "4.5", "10", "20", "A popular shirt", false, null));

        add(new ProductDto(4, "Golf pants", "golf-pants", "pants", "/images/pants1.jpg",
                "90", "Oliver", "4.5", "10", "20", "A popular shirt", false, null));

        add(new ProductDto(5, "Classic pants", "classic-pants", "pants", "/images/pants2.jpg",
                "95", "Zara", "4.5", "10", "20", "Smart looking pants", false, null));

        add(new ProductDto(6, "Fit pants", "fit-pants", "pants", "/images/pants3.jpg",
                "75", "Casely", "4.5", "10", "20", "A popular shirt", false, null));
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
        return objectMapper.writeValueAsString(new ArrayList<>(productDtoList));
    }

    @GetMapping(value = "/find/by-id")
    public ResponseEntity<String> getItemById(@RequestParam Integer id, @RequestHeader MultiValueMap<String, String> headers) throws JsonProcessingException {


        ProductDto item = productDtoList.stream().filter(i -> Objects.equals(i.getId(), id)).findAny().orElse(null);
        ObjectMapper objectMapper = new ObjectMapper();
        return new ResponseEntity<>(objectMapper.writeValueAsString(item), HttpStatus.OK);
    }

    @GetMapping(value = "/find/byCategory")
    public ResponseEntity<String> getByCategory(@RequestParam String category,
                                                @RequestParam String query,
                                                @RequestHeader MultiValueMap<String, String> headers) throws JsonProcessingException {

        List<ProductDto> collect = productDtoList.stream().filter(i -> i.getCategory().contains(category)).collect(Collectors.toList());

        if (!StringUtils.isEmpty(query)){
            collect = productDtoList.stream().filter(i -> i.getName().contains(query)).collect(Collectors.toList());
        }
        ObjectMapper objectMapper = new ObjectMapper();
        return new ResponseEntity<>(objectMapper.writeValueAsString(collect), HttpStatus.OK);
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

    @DeleteMapping(value = "/products/{id}")
    public String deleteProduct(@PathVariable("id") Integer id) throws JsonProcessingException, IllegalAccessException {
        productDtoList = productDtoList.stream().filter(productDto -> productDto.getId() != id).collect(Collectors.toList());
        return "true";
    }

    @PostMapping(value = "/products")
    public String addProduct(@RequestBody ProductDto productDto) {
        productDtoList.add(productDto);
        return "true";
    }

    @PutMapping(value = "/products")
    public ProductDto update(@RequestBody ProductDto productDto) {
        ProductDto toReturn = productDto;
        AtomicBoolean replaced = new AtomicBoolean(false);
        productDtoList = productDtoList.stream().map(p -> {
            if (Objects.equals(productDto.getId(), p.getId())) {
                replaced.set(true);
                return productDto;
            } else {
                return p;
            }
        }).collect(Collectors.toList());
        return replaced.get() ? toReturn : null;
    }

    @GetMapping(value = "/getOrderById/{id}")
    public String getOrdersById(@PathParam("id") String id, @RequestBody String map) throws JsonProcessingException, IllegalAccessException {
        ObjectMapper objectMapper = new ObjectMapper();
        for (String order : listOrders) {
            if (order.contains(id))
                return objectMapper.writeValueAsString(order);
        }

        throw new IllegalAccessException("not found");


    }

    @PostMapping(value = "/postDelivery/{id}")
    public String postDelivery(@PathVariable("id") String id,
                               @RequestBody String map) throws JsonProcessingException, IllegalAccessException {
        ObjectMapper objectMapper = new ObjectMapper();
        for (int i = 0; i < listOrders.size(); i++) {
            String order = listOrders.get(i);
            if (order.contains(id)) {
                listOrders.remove(i);
                listOrders.add(map);
                break;
            }
        }

        for (String order : listOrders) {
            if (order.contains(id))
                return objectMapper.writeValueAsString(order);
        }

        throw new IllegalAccessException("not found");


    }


    @GetMapping(value = "/getSummary")
    public String getSummary() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(Summary.builder()
                .productsCount("4")
                .ordersCount("2")
                .ordersPrice("5342")
                .usersCount("3")
                .salesData(new ArrayList<SalesData>() {{
                    add(SalesData.builder()._id("noiembrie in plm")
                            .totalSales("6000")
                            .build());
                    add(SalesData.builder()._id("decembrie fra")
                            .totalSales("3000")
                            .build());
                }})
                .build());
    }


    @PutMapping(value = "/putUserInfo", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String getOrders(@RequestBody UserInfo userInfo) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(userInfo);
    }

    @GetMapping(value = "/orders")
    public String getOrders(@RequestParam String id) throws JsonProcessingException {


        ObjectMapper objectMapper = new ObjectMapper();
        for (String order : listOrders) {
            if (order.contains(id))
                return order;
        }
        if (StringUtils.isEmpty(currentOrder)) {
            return listOrders.get(0);
        }

        return currentOrder;
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
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
        try {
            return (List<String>) ois.readObject();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


}
