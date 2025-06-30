package com.example.demo1.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo1.Modal.User;
public interface UserRepository extends JpaRepository<User,Integer> {
    public User findByEmail(String email);

    public Boolean existsByEmail(String email);
}

