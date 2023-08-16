package com.backend.ecommerce.dao;

import com.backend.ecommerce.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface CartDao {

    public Cart find(int id);

    public void update(Cart cart);
}
