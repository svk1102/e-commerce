package com.backend.ecommerce.service;

import com.backend.ecommerce.dao.OrderDao;
import com.backend.ecommerce.dao.ProductDao;
import com.backend.ecommerce.dao.UserDao;
import com.backend.ecommerce.dto.*;
import com.backend.ecommerce.entity.Category;
import com.backend.ecommerce.entity.Order;
import com.backend.ecommerce.entity.Product;
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
public class OrderService {
    @Autowired
    OrderDao orderDao;

    @Autowired
    UserService userService;

    @Autowired
    UserDao userDao;

    @Autowired
    ProductService productService;
    @Autowired
    CategoryService categoryService;

    @Autowired
    EntityManager entityManager;


    @Transactional
    public OrderResponse placeOrder(OrderRequest order) throws Exception {
        UserResponse user = userService.getUserById(order.getUserId());
        if(user == null){
            throw new Exception("User not found ");
        }

        order.getProducts().forEach((key,value) -> {
            ProductResponse product = productService.getProductById(key);
            if(product.getQuantity() < value){
                try {
                    throw new Exception("Product Quantity not available");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }else {
                product.setQuantity(product.getQuantity() - value);
                ProductUpdateRequest product1 = new ProductUpdateRequest();
                product1.setUserId(product.getUserId());
                product1.setCategory(product.getCategory());
                product1.setProductId(product.getProductId());
                product1.setName(product.getName());
                product1.setQuantity(product.getQuantity());
                product1.setPrice(product.getPrice());
                product1.setImageUrl(product.getImageUrl());
                try {
                    productService.updateProduct(product1);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Order order1 = new Order();
        order1.setPrice(order.getPrice());
        order1.setTimeStamp(order.getTimeStamp());
        order1.setOrderItems(order.getProducts());
        order1.setUser(userDao.findById(order.getUserId()));
        user.getCart().setCartItems(new HashMap<Integer,Integer>());

        return orderToOrderResponse(orderDao.save(order1));
    }

    public OrderResponse getOrder(Integer id){
        return orderToOrderResponse(orderDao.getOrderById(id));
    }
    public List<OrderResponse> getAllOrders(){
        return orderDao.getAll().stream().map(this::orderToOrderResponse).toList();
    }

    public List<OrderResponse> findOrderByUserId(int id){
        return orderDao.getOrderByUser(id).stream().map(this::orderToOrderResponse).toList();
    }

    public List<OrderResponse> findOrderByParam(String param) throws UnsupportedEncodingException, JsonProcessingException {
        String decodedParam = URLDecoder.decode(param, "UTF-8");
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> queryParams = objectMapper.readValue(decodedParam, new TypeReference<Map<String, String>>() {});

        // Now queryParams will contain your individual parameters
        String orderId = queryParams.get("id");
        String userId = queryParams.get("user");
        String startDate = queryParams.get("startDate");
        String endDate = queryParams.get("endDate");

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> orderRoot = criteriaQuery.from(Order.class);

        List<Predicate> predicates = new ArrayList<>();


        if(orderId != null && !orderId.isEmpty()){
            predicates.add(criteriaBuilder.equal(orderRoot.get("id"),Integer.valueOf(orderId)));
        }
        if(userId != null && !userId.isEmpty()){
            predicates.add(criteriaBuilder.equal(orderRoot.get("user").get("id"),Integer.valueOf(userId)));
        }

        if (startDate!= null && !startDate.isEmpty()) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(orderRoot.get("timeStamp"), Integer.parseInt(startDate)));
        }

        if (endDate!= null && !endDate.isEmpty()) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(orderRoot.get("timeStamp"), Integer.parseInt(endDate)));
        }

        criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
        List<Order> orders = entityManager.createQuery(criteriaQuery).getResultList();
        return orders.stream().map(this::orderToOrderResponse).toList();
    }



    public OrderResponse orderToOrderResponse(Order order){
        if(order == null){
            return null;
        }
        return OrderResponse.builder()
                .id(order.getId())
                .price(order.getPrice())
                .timeStamp(order.getTimeStamp())
                .products(order.getOrderItems())
                .userId(order.getUser().getId())
                .build();

    }
}
