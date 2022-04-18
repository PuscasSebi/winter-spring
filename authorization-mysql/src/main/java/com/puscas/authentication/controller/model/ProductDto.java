package com.puscas.authentication.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ProductDto {
    Integer id;
    String name;
    String slug;
    String category;
    String image;
    String price;
    String brand;
    String rating;
    String numReviews;
    String countInStock;
    String description;
}
