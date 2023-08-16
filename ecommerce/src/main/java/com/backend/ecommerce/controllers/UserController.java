package com.backend.ecommerce.controllers;

import com.backend.ecommerce.dto.OrderResponse;
import com.backend.ecommerce.dto.ProductResponse;
import com.backend.ecommerce.dto.UserResponse;
import com.backend.ecommerce.entity.User;
import com.backend.ecommerce.service.OrderService;
import com.backend.ecommerce.service.ProductService;
import com.backend.ecommerce.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;


    @GetMapping("/users")
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<UserResponse> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    public UserResponse getUser(@PathVariable int id){
        UserResponse user = userService.getUserById(id);
        if(user == null){
            throw new RuntimeException("User Id not found : " + id);
        }
        return user;
    }


    @PutMapping("/users")
    public UserResponse updateUser(@RequestBody User user){
        return userService.updateUser(user);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id){
        UserResponse user = userService.getUserById(id);
        if(user == null){
            throw new RuntimeException("User id not found -"+id);
        }
        userService.deleteById(id);

    }

    @GetMapping("/users/{id}/products")
    public List<ProductResponse> getProductsByMerchant(@PathVariable int id){
        UserResponse user = userService.getUserById(id);
        if(user == null){
            throw new RuntimeException("User id not found -"+id);
        }
        List<ProductResponse> products = productService.findProductByMerchant(id);
        return products;
    }

    @GetMapping("/users/{id}/orders")
    public List<OrderResponse> getOrdersByUserId(@PathVariable int id){
        System.out.println(id);
        UserResponse user = userService.getUserById(id);
        if(user == null){
            throw new RuntimeException("User id not found -"+id);
        }
        List<OrderResponse> orders = orderService.findOrderByUserId(id);
        return orders;
    }

    @GetMapping("/users/merchants")
    public List<UserResponse> getAllMerchants(){
        return userService.getAllMerchants();
    }

}
