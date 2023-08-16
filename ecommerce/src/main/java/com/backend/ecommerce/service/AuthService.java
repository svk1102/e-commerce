package com.backend.ecommerce.service;

import com.backend.ecommerce.config.JwtService;
import com.backend.ecommerce.dao.UserDao;
import com.backend.ecommerce.dto.AuthRequest;
import com.backend.ecommerce.dto.AuthResponse;
import com.backend.ecommerce.dto.UserRequest;
import com.backend.ecommerce.dto.UserResponse;
import com.backend.ecommerce.entity.Cart;
import com.backend.ecommerce.entity.Role;
import com.backend.ecommerce.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Autowired
    UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse registerUser(UserRequest request) {
        var user = new User();
        user.setId(0);
        user.setRole(Role.USER);
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setProducts(new ArrayList<>());
        user.setCart(new Cart());
        userDao.save(user);
         var jwtToken = jwtService.generateToken(user);
         return AuthResponse.builder()
                 .token(jwtToken)
                 .build();
    }


    @Transactional
    public AuthResponse registerMerchant(UserRequest request) {
        var user = new User();
        user.setId(0);
        user.setRole(Role.MERCHANT);
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setProducts(new ArrayList<>());
        user.setCart(new Cart());
        userDao.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword())
        );
        var user = userDao.findByEmail(request.getEmail());
        System.out.println(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }
}
