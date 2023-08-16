package com.backend.ecommerce.service;

import com.backend.ecommerce.dao.CategoryDao;
import com.backend.ecommerce.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    CategoryDao categoryDao;

    public List<Category> getAllCategories(){
        return categoryDao.findAll();
    }

    public Category getCategoryById(int id){
        return categoryDao.findById(id);
    }

    @Transactional
    public Category addCategory(Category category){
        Category category1 = new Category();
        category1.setName(category.getName());
        category1.setImageUrl(category.getImageUrl());
        category1.setId(0);
        category1.setProducts(new ArrayList<>());
        return categoryDao.addCategory(category1);
    }

    @Transactional
    public void deleteCategory(int id){
        categoryDao.deleteCategory(id);
    }

    public Category findCategoryByName(String name){
        return categoryDao.findByName(name);
    }
}
