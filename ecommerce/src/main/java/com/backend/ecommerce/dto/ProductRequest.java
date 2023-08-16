package com.backend.ecommerce.dto;

import com.backend.ecommerce.entity.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductRequest {

    private String name;
    private String category;
    private int price;
    private int quantity;
    private Integer userId;
    private String imageUrl;
}

