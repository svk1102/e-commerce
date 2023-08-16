package com.backend.ecommerce.service;

import com.backend.ecommerce.dao.ProductDao;
import com.backend.ecommerce.dao.UserDao;
import com.backend.ecommerce.dto.ProductRequest;
import com.backend.ecommerce.dto.ProductResponse;
import com.backend.ecommerce.dto.ProductUpdateRequest;
import com.backend.ecommerce.dto.UserResponse;
import com.backend.ecommerce.entity.Category;
import com.backend.ecommerce.entity.Product;
import com.backend.ecommerce.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

@Service
public class ProductService {
    @Autowired
    ProductDao productDao;

    @Autowired
    UserDao userDao;

    @Autowired
    UserService userService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    EntityManager entityManager;

    public List<ProductResponse> getAllProducts(){
        return productDao.findAll().stream().map(this::productToProductResponse).toList();
    }
    public ProductResponse getProductById(int theId){
        return productToProductResponse(productDao.findById(theId));
    }

    @Transactional
    public ProductResponse addProduct(ProductRequest product) throws Exception {
        User user = userDao.findById(product.getUserId());
        if(user == null){
            throw new Exception("User Id not found : " + product.getUserId());
        }
        Product product1 = new Product();
        product1.setCategory(categoryService.findCategoryByName(product.getCategory()));
        product1.setName(product.getName());
        product1.setPrice(product.getPrice());
        product1.setQuantity(product.getQuantity());
        product1.setImageUrl(product.getImageUrl());
        product1.setUser(user);
        product1.setId(0);
        product1.setLikes(new HashSet<Integer>());
        return productToProductResponse(productDao.save(product1));
    }
    @Transactional
    public ProductResponse updateProduct(ProductUpdateRequest product) throws Exception {
        User user = userDao.findById(product.getUserId());
        if(user == null){
            throw new Exception("User Id not found : " + product.getUserId());
        }
        Product product1 = new Product();
        product1.setCategory(categoryService.findCategoryByName(product.getCategory()));
        product1.setName(product.getName());
        product1.setPrice(product.getPrice());
        product1.setQuantity(product.getQuantity());
        product1.setImageUrl(product.getImageUrl());
        product1.setUser(user);
        product1.setId(product.getProductId());
        product1.setLikes(productDao.findById(product.getUserId()).getLikes());
        return productToProductResponse(productDao.save(product1));
    }

    @Transactional
    public void deleteById(int id){
        productDao.deleteById(id);
    }

    public List<ProductResponse> findProductByMerchant(int id){
        return productDao.findProductByMerchant(id).stream().map(this::productToProductResponse).toList();
    }

    public List<ProductResponse> findProductByCategory(String name){
        return productDao.findProductByCategory(name).stream().map(this::productToProductResponse).toList();
    }

    @Transactional
    public void likeProduct(int productId,int userId){
        productDao.likeProduct(productId,userId);
    }


    public ProductResponse productToProductResponse(Product product){
        if(product == null){
            return null;
        }
        return ProductResponse.builder()
                .productId(product.getId())
                .price(product.getPrice())
                .name(product.getName())
                .category(product.getCategory().getName())
                .imageUrl(product.getImageUrl())
                .userId(product.getUser().getId())
                .quantity(product.getQuantity())
                .likes(product.getLikes())
                .build();

    }

    public List<ProductResponse> searchProductsByQuery(List<Integer> categoryIds,List<Integer> merchantIds,Integer minPrice , Integer maxPrice) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
        Root<Product> productRoot = criteriaQuery.from(Product.class);

        Predicate predicateFinal = criteriaBuilder.conjunction();


        if (categoryIds != null  && !categoryIds.isEmpty()) {
            List<Category> categories = new ArrayList<>();
            for (Integer categoryId : categoryIds) {
                categories.add(categoryService.getCategoryById(categoryId));
            }
            Predicate predicateForCategory = productRoot.get("category").in(categories);
            predicateFinal = criteriaBuilder.and(predicateFinal, predicateForCategory);
        }




        if (merchantIds != null && !merchantIds.isEmpty()) {
            List<User> users = new ArrayList<>();
            for (Integer merchantId : merchantIds) {
                users.add(userDao.findById(merchantId));
            }
            Predicate predicateForMerchant = productRoot.get("user").in(users);
            predicateFinal = criteriaBuilder.and(predicateFinal, predicateForMerchant);
        }

        if (minPrice != null) {
            // Create min price predicate
            Predicate predicateForMinPrice = criteriaBuilder.ge(productRoot.get("price"), minPrice);
            predicateFinal = criteriaBuilder.and(predicateFinal, predicateForMinPrice);
        }

        if (maxPrice != null) {
            // Create max price predicate
            Predicate predicateForMaxPrice = criteriaBuilder.le(productRoot.get("price"), maxPrice);
            predicateFinal = criteriaBuilder.and(predicateFinal, predicateForMaxPrice);
        }



//        Predicate predicateFinal = criteriaBuilder.and(predicateForMerchant, predicateForCategory);
        criteriaQuery.where(predicateFinal);
        List<Product> products = entityManager.createQuery(criteriaQuery).getResultList();
        return products.stream().map(this::productToProductResponse).toList();
    }


    public List<ProductResponse> searchProductsBySingleParam(String param) throws UnsupportedEncodingException, JsonProcessingException {
        String decodedParam = URLDecoder.decode(param, "UTF-8");
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> queryParams = objectMapper.readValue(decodedParam, new TypeReference<Map<String, String>>() {});

        // Now queryParams will contain your individual parameters
        String categoryIds = queryParams.get("categoryIds");
        String merchantIds = queryParams.get("merchantIds");
        String minPrice = queryParams.get("minPrice");
        String maxPrice = queryParams.get("maxPrice");



        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
        Root<Product> productRoot = criteriaQuery.from(Product.class);

        Predicate predicateFinal = criteriaBuilder.conjunction();

        if (!categoryIds.isEmpty()) {
            int[] categoryIdArray = Arrays.stream(categoryIds.split(","))
                    .mapToInt(Integer::parseInt)
                    .toArray();
            List<Category> categories = new ArrayList<>();
            for (Integer categoryId : categoryIdArray) {
                categories.add(categoryService.getCategoryById(categoryId));
            }
            Predicate predicateForCategory = productRoot.get("category").in(categories);
            predicateFinal = criteriaBuilder.and(predicateFinal, predicateForCategory);
        }

        if (!merchantIds.isEmpty()) {
            int[] merchantIdArray = Arrays.stream(merchantIds.split(","))
                    .mapToInt(Integer::parseInt)
                    .toArray();
            List<User> users = new ArrayList<>();
            for (Integer merchantId : merchantIdArray) {
                users.add(userDao.findById(merchantId));
            }
            Predicate predicateForMerchant = productRoot.get("user").in(users);
            predicateFinal = criteriaBuilder.and(predicateFinal, predicateForMerchant);
        }

        if (!minPrice.isEmpty()) {
            // Create min price predicate
            Predicate predicateForMinPrice = criteriaBuilder.ge(productRoot.get("price"), Integer.valueOf(minPrice));
            predicateFinal = criteriaBuilder.and(predicateFinal, predicateForMinPrice);
        }

        if (!maxPrice.isEmpty()) {
            // Create max price predicate
            Predicate predicateForMaxPrice = criteriaBuilder.le(productRoot.get("price"),Integer.valueOf(maxPrice));
            predicateFinal = criteriaBuilder.and(predicateFinal, predicateForMaxPrice);
        }

        criteriaQuery.where(predicateFinal);
        List<Product> products = entityManager.createQuery(criteriaQuery).getResultList();
        return products.stream().map(this::productToProductResponse).toList();
    }

    public List<ProductResponse> searchProductsDecoded(String param){
        String[] temp = param.split(";");
       var categoryIds = temp[0].split(":");
        String[] categoryIdsArray = ((categoryIds)[1].split(","));

        var merchantIds = temp[1].split(":");
        String[] merchantIdsArray = ((merchantIds)[1].split(","));

        System.out.println(Arrays.toString(categoryIdsArray));
        System.out.println(Arrays.toString(merchantIdsArray));
        System.out.println(temp[2].split(":")[1]);
        System.out.println(temp[3].split(":")[1]);
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
        Root<Product> productRoot = criteriaQuery.from(Product.class);

        return null;
    }

}

