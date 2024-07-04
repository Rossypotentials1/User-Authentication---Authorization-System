package com.rossypotentials.springsecurityjwt.controller;

import com.rossypotentials.springsecurityjwt.dto.RequestResponse;
import com.rossypotentials.springsecurityjwt.entity.Product;
import com.rossypotentials.springsecurityjwt.repository.ProductRepository;
import com.rossypotentials.springsecurityjwt.repository.UserRepository;
import com.rossypotentials.springsecurityjwt.service.impl.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AdminUsersController {


    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final AuthServiceImpl authServiceImpl;

    @GetMapping("/public/product")
    public ResponseEntity<Object> getAllProducts(){
        return ResponseEntity.ok(productRepository.findAll());
    }


    @PostMapping("/admin/saveProduct")
    public ResponseEntity<Object> saveProduct(@RequestBody RequestResponse productRequest){
        Product product = new Product();
        product.setProductName(productRequest.getName());
        return ResponseEntity.ok(productRepository.save(product));
    }

    @GetMapping("/user/view-profile")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Object> userAlone() {
        RequestResponse userResponse = authServiceImpl.getAuthenticatedUserDetails();
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("/admin-user")
    public ResponseEntity<Object> bothAdminAndUserApi(){
        return ResponseEntity.ok(" Both Admin and user can access this API");
    }

    @DeleteMapping("/admin/deleteUser/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> deleteUser(@PathVariable Integer userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            return ResponseEntity.ok("User deleted successfully");
        } else {
            return ResponseEntity.status(404).body("User not found");
        }
    }
}
