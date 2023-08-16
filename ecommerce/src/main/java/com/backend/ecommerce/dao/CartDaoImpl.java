package com.backend.ecommerce.dao;

import com.backend.ecommerce.entity.Cart;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CartDaoImpl implements CartDao{

    private EntityManager entityManager;

    @Autowired
    public CartDaoImpl(EntityManager theEntityManager){
        entityManager = theEntityManager;
    }

    @Override
    public Cart find(int id) {
        return entityManager.find(Cart.class,id);
    }

    @Override
    public void update(Cart cart) {
        entityManager.merge(cart);
    }
}
