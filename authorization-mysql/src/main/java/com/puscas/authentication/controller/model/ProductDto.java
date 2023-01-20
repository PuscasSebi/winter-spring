package com.puscas.authentication.controller.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
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
    boolean isFeatured;
    String featuredImage;
}
