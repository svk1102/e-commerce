package com.backend.ecommerce.controllers;

import com.backend.ecommerce.dao.UserDao;
import com.backend.ecommerce.dto.CartRequest;
import com.backend.ecommerce.dto.MessageResponse;
import com.backend.ecommerce.entity.Cart;
import com.backend.ecommerce.service.CartService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin(origins = "*")
public class CartController {

    @Autowired
    CartService cartService;

    @Autowired
    UserDao userDao;

    @PostMapping("/carts")
    public ResponseEntity<MessageResponse> addToCart(@RequestBody CartRequest cart) throws Exception {
        return new ResponseEntity<>(new MessageResponse(cartService.addToCart(cart.getUserId(),cart.getProductId())), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/carts/{productId}")
    public ResponseEntity<MessageResponse> removeFromCart(@PathVariable int productId , Principal principal) throws Exception{
        Integer id = userDao.findByEmail(principal.getName()).getId();
        return new ResponseEntity<>(new MessageResponse(cartService.removeFromCart(id,productId)),HttpStatus.ACCEPTED);
    }

    @GetMapping("/carts/{id}")
    public Cart getCartFromUserId(@PathVariable int id) throws Exception {
        return cartService.getCartByUserId(id);
    }
}
