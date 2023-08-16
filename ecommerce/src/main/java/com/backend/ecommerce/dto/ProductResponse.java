package com.backend.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

    private String name;
    private String category;
    private int price;
    private int quantity;
    private Integer userId;
    private String imageUrl;
    private Integer productId;
    private Set<Integer> likes;
}
