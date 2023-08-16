package com.backend.ecommerce.dao;

import com.backend.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface UserDao{
    List<User> findAll();

    User findById(int theId);

    User save(User user);

    void deleteById(int theId);

    User findByEmail(String email);

    List<User> findAllMerchants();
}
