package com.backend.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class OrderRequest {
    private Integer userId;
    private Integer price;
    private Integer timeStamp;

    private Map<Integer,Integer> products;

}
