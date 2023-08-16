package com.backend.ecommerce.service;

import com.backend.ecommerce.dao.UserDao;
import com.backend.ecommerce.dto.UserResponse;
import com.backend.ecommerce.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    @Autowired
    UserDao userDao;


    public List<UserResponse> getAllUsers(){
        return userDao.findAll().stream().map(this::userToUserResponse).toList();
    }
    public UserResponse getUserById(int theId){
        return userToUserResponse(userDao.findById(theId));
    }

    public UserResponse getUserByEmail(String email){
        return userToUserResponse(userDao.findByEmail(email));
    }
    @Transactional
    public UserResponse updateUser(User user){
        return userToUserResponse(userDao.save(user));
    }

    @Transactional
    public void deleteById(int id){
        userDao.deleteById(id);
    }

    public List<UserResponse> getAllMerchants(){
        return userDao.findAllMerchants().stream().map(this::userToUserResponse).toList();
    }

    public UserResponse userToUserResponse(User user){
        if(user == null){
            return null;
        }

        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .cart(user.getCart())
                .build();
    }


}
