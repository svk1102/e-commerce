package com.backend.ecommerce.dao;

import com.backend.ecommerce.dto.OrderResponse;
import com.backend.ecommerce.entity.Order;
import com.backend.ecommerce.entity.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderDaoImpl implements OrderDao{

    private EntityManager entityManager;

    @Autowired
    public OrderDaoImpl(EntityManager theEntityManager){
        entityManager = theEntityManager;
    }


    @Override
    public List<Order> getAll() {
        TypedQuery<Order> theQuery = entityManager.createQuery("from Order",Order.class);
        return theQuery.getResultList();
    }

    @Override
    public Order getOrderById(Integer id) {
        return entityManager.find(Order.class,id);
    }

    @Override
    public Order save(Order order) {
        return entityManager.merge(order);
    }

    @Override
    public List<Order> getOrderByUser(int id) {
        TypedQuery<Order> query = entityManager.createQuery(
                "from Order where user.id=:data", Order.class
        );
        query.setParameter("data",id);

        List<Order> orders = query.getResultList();
        return orders;
    }
}
