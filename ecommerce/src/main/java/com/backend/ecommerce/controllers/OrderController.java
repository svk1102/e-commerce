package com.backend.ecommerce.controllers;

import com.backend.ecommerce.dto.OrderRequest;
import com.backend.ecommerce.dto.OrderResponse;
import com.backend.ecommerce.service.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping("/orders")
    public OrderResponse placeOrder(@RequestBody OrderRequest order) throws Exception {
        return orderService.placeOrder(order);
    }

    @GetMapping("/orders")
    public List<OrderResponse> getAllOrders(){
        return orderService.getAllOrders();
    }

    @GetMapping("/orders/{id}")
    public OrderResponse getOrderById(@PathVariable int id){
        return orderService.getOrder(id);
    }

    @GetMapping("/orders/filter")
    public List<OrderResponse> getFilteredOrders(@RequestParam String param) throws UnsupportedEncodingException, JsonProcessingException {
        return orderService.findOrderByParam(param);
    }
}
