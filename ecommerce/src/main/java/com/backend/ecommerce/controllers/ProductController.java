package com.backend.ecommerce.controllers;
import com.backend.ecommerce.dao.UserDao;
import com.backend.ecommerce.dto.ProductRequest;
import com.backend.ecommerce.dto.ProductResponse;
import com.backend.ecommerce.dto.ProductUpdateRequest;
import com.backend.ecommerce.entity.Product;
import com.backend.ecommerce.entity.User;
import com.backend.ecommerce.service.ProductService;
import com.backend.ecommerce.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin(origins = "*")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private UserDao userDao;

    @GetMapping("/products")
    public List<ProductResponse> getAllProducts(){
        return productService.getAllProducts();
    }

    @GetMapping("/products/{id}")
    public ProductResponse getProduct(@PathVariable int id){
        ProductResponse product = productService.getProductById(id);
        if(product == null){
            throw new RuntimeException("Product Id not found : " + id);
        }
        return product;
    }

    @PostMapping("/products")
    public ProductResponse addProduct(@RequestBody ProductRequest product , Principal principal) throws Exception {
        System.out.println(principal.getName());
        Integer id = userDao.findByEmail(principal.getName()).getId();
        product.setUserId(id);
        return productService.addProduct(product);

    }

    @PutMapping("/products")
    public ProductResponse updateProduct(@RequestBody ProductUpdateRequest product) throws Exception {
        return productService.updateProduct(product);
    }

    @DeleteMapping("/products/{id}")
    public void deleteProduct(@PathVariable int id){
        ProductResponse product = productService.getProductById(id);
        if(product == null){
            throw new RuntimeException("Product id not found -"+id);
        }
        productService.deleteById(id);
    }

    @GetMapping("/products/{productId}/like")
    public void likeProduct(@PathVariable int productId,Principal principal){
        int id = userDao.findByEmail(principal.getName()).getId();
        productService.likeProduct(productId,id);
    }

    @GetMapping("/products/filter")
    public List<ProductResponse> filter(@RequestParam(required = false) List<Integer> categoryIds , @RequestParam(required = false) List<Integer> merchantIds , @RequestParam(required = false) Integer minPrice , @RequestParam(required = false) Integer maxPrice){
        return productService.searchProductsByQuery(categoryIds,merchantIds,minPrice,maxPrice);
    }

    @GetMapping("/products/query")
    public List<ProductResponse> filter(@RequestParam String param) throws UnsupportedEncodingException, JsonProcessingException {
        return productService.searchProductsBySingleParam(param);
    }

    @GetMapping("/products/search")
    public List<ProductResponse> filterBySingleParam(@RequestParam String param) throws UnsupportedEncodingException, JsonProcessingException {
        return productService.searchProductsDecoded(param);
    }
}
