package com.backend.ecommerce.dao;

import com.backend.ecommerce.entity.Product;


import java.util.List;

public interface ProductDao {
    List<Product> findAll();

    Product findById(int theId);

    Product save(Product product);

    void deleteById(int theId);

    List<Product> findProductByMerchant(int id);

    List<Product> findProductByCategory(String name);

    void likeProduct(int productId,int userId);
}
