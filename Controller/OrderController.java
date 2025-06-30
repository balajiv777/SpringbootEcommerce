package com.example.demo1.Controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo1.Modal.Order;
import com.example.demo1.Modal.Product;
import com.example.demo1.Modal.User;
import com.example.demo1.Repository.OrderRepository;
import com.example.demo1.Repository.ProductRepository;
import com.example.demo1.Repository.UserRepository;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    
    @Autowired
    OrderRepository orderRepository;
    
    @Autowired
    UserRepository userRepository;
    
    @Autowired
    ProductRepository productRepository;

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        try {
            List<Order> orders = orderRepository.findAll();
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable("id") int id) {
        Optional<Order> orderData = orderRepository.findById(id);
        return orderData.map(order -> new ResponseEntity<>(order, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequest orderRequest) {
        try {
            Optional<User> userData = userRepository.findById(orderRequest.getUserId());
            Optional<Product> productData = productRepository.findById(orderRequest.getProductId());
            
            if (userData.isPresent() && productData.isPresent()) {
                Product product = productData.get();
                if (product.getStockQuantity() < orderRequest.getQuantity()) {
                    return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
                }
                
                product.setStockQuantity(product.getStockQuantity() - orderRequest.getQuantity());
                productRepository.save(product);
                
                Order order = new Order(
                    userData.get(),
                    product,
                    orderRequest.getQuantity(),
                    product.getPrice() * orderRequest.getQuantity(),
                    LocalDateTime.now(),
                    "PENDING"
                );
                
                Order _order = orderRepository.save(order);
                return new ResponseEntity<>(_order, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable("id") int id, @RequestBody String status) {
        Optional<Order> orderData = orderRepository.findById(id);
        if (orderData.isPresent()) {
            Order _order = orderData.get();
            _order.setStatus(status);
            return new ResponseEntity<>(orderRepository.save(_order), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUser(@PathVariable("userId") int userId) {
        try {
            Optional<User> userData = userRepository.findById(userId);
            if (userData.isPresent()) {
                List<Order> orders = orderRepository.findByUser(userData.get());
                return new ResponseEntity<>(orders, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

class OrderRequest {
    private int userId;
    private int productId;
    private int quantity;
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public int getProductId() {
        return productId;
    }
    
    public void setProductId(int productId) {
        this.productId = productId;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}