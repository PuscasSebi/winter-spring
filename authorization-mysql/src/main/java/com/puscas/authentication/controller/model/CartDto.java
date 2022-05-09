package com.puscas.authentication.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDto {
    List<ProductDto> productDtos = new ArrayList<>();
}
