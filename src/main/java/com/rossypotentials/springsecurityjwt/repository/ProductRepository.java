package com.rossypotentials.springsecurityjwt.repository;

import com.rossypotentials.springsecurityjwt.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
