package com.backend.ecommerce.controllers;

import com.backend.ecommerce.dto.AuthRequest;
import com.backend.ecommerce.dto.AuthResponse;
import com.backend.ecommerce.dto.UserRequest;
import com.backend.ecommerce.dto.UserResponse;
import com.backend.ecommerce.entity.User;
import com.backend.ecommerce.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/registerUser")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody UserRequest request){
        return ResponseEntity.ok(authService.registerUser(request));
    }

    @PostMapping("/registerMerchant")
    public ResponseEntity<AuthResponse> registerMerchant(@RequestBody UserRequest request){
        return ResponseEntity.ok(authService.registerMerchant(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request){
        return ResponseEntity.ok(authService.login(request));
    }
}
