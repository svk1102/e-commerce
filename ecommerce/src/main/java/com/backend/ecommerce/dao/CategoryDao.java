package com.backend.ecommerce.dao;

import com.backend.ecommerce.entity.Category;
import com.backend.ecommerce.entity.Product;

import java.util.List;

public interface CategoryDao {

    public List<Category> findAll();

    public Category addCategory(Category category);
    public void deleteCategory(int id);
    public Category findById(int id);

    public Category findByName(String name);
}
