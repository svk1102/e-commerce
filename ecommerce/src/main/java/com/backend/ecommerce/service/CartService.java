package com.backend.ecommerce.service;

import com.backend.ecommerce.dao.CartDao;
import com.backend.ecommerce.dto.UserResponse;
import com.backend.ecommerce.entity.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class CartService {

    @Autowired
    CartDao cartDao;

    @Autowired
    UserService userService;
    @Autowired
    ProductService productService;

    @Transactional
    public String addToCart(Integer userId,Integer productId) throws Exception {
        if(userService.getUserById(userId) == null || productService.getProductById(productId) ==null){
            throw new Exception("Invalid Ids passed");
        }
        Cart cart = cartDao.find(userId);
        Map<Integer,Integer> cartItems = cart.getCartItems();
        if (cartItems.containsKey(productId)){
            Integer quantity = cartItems.get(productId);
            cartItems.put(productId,quantity+1);
        }else {
            cartItems.put(productId,1);
        }
        Cart cart1 = new Cart();
        cart1.setId(cart.getId());
        cart1.setUser(cart.getUser());
        cart1.setCartItems(cartItems);
        cartDao.update(cart1);
        return "Added to Cart";
    }

    @Transactional
    public String removeFromCart(Integer userId , Integer productId) throws Exception {
        if(userService.getUserById(userId) == null || productService.getProductById(productId) ==null){
            throw new Exception("Invalid Ids passed");
        }
        Cart cart = cartDao.find(userId);
        Map<Integer,Integer> cartItems = cart.getCartItems();

        if(cartItems.containsKey(productId)){
            Integer quantity = cartItems.get(productId);
            if(quantity == 1){
                cartItems.remove(productId);
            }
            else {
                cartItems.put(productId,quantity-1);
            }
        }

        return "Removed from cart successfully";
    }

    public Cart getCartByUserId(int id) throws Exception {
        if(userService.getUserById(id) == null){
            throw new Exception("Invalid user id");
        }
        return userService.getUserById(id).getCart();
    }

}
