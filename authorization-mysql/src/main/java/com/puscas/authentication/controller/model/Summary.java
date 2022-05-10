package com.puscas.authentication.controller.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
public class Summary {
    String ordersCount;
    String usersCount;
    String ordersPrice;
    String productsCount;
    List<SalesData> salesData;
}
