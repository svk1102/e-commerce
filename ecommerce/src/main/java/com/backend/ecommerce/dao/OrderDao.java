package com.backend.ecommerce.dao;

import com.backend.ecommerce.dto.OrderResponse;
import com.backend.ecommerce.entity.Order;

import java.util.List;

public interface OrderDao {
    public List<Order> getAll();
    public Order getOrderById(Integer id);
    public Order save(Order order);

    public List<Order> getOrderByUser(int id);
}
