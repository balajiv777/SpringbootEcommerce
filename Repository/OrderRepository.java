package com.example.demo1.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo1.Modal.Order;
import com.example.demo1.Modal.User;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findByUser(User user);

    List<Order> findByStatus(String status);
    List<Order> findByProductId(int productId);
    List<Order> findByUserAndStatus(User user, String status);
}