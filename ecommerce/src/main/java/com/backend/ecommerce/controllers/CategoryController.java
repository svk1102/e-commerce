package com.backend.ecommerce.controllers;

import com.backend.ecommerce.dto.ProductResponse;
import com.backend.ecommerce.entity.Category;
import com.backend.ecommerce.entity.Product;
import com.backend.ecommerce.service.CategoryService;
import com.backend.ecommerce.service.ProductService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin(origins = "*")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    ProductService productService;

    @GetMapping("/category")
    public List<Category> getAllCategory(){
        return categoryService.getAllCategories();
    }

    @GetMapping("/category/{name}")
    public Category findCategoryByName(@PathVariable String name){
        return categoryService.findCategoryByName(name);
    }

    @GetMapping("/category/{id}")
    public Category findCategoryById(@PathVariable int id){
        return categoryService.getCategoryById(id);
    }

    @PostMapping("/category")
    public Category addCategory( @RequestBody Category category){
        return categoryService.addCategory(category);
    }

    @GetMapping("/category/{name}/products")
    public List<ProductResponse> getProductsByCategory(@PathVariable String name){
        return productService.findProductByCategory(name);
    }
}
