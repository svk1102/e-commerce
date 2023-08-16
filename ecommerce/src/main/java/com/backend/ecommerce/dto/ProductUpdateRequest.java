package com.backend.ecommerce.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductUpdateRequest {
    private String name;
    private String category;
    private int price;
    private int quantity;
    private Integer userId;
    private String imageUrl;
    private int productId;
}
