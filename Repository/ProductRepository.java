package com.example.demo1.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo1.Modal.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
   
    List<Product> findByCategory(String category);
    List<Product> findByNameContainingIgnoreCase(String name);
}
