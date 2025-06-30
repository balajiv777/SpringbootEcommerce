package com.example.demo1.Controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo1.Modal.User;
import com.example.demo1.Repository.UserRepository;


@RestController
@RequestMapping("/api")

public class UserController {
    @Autowired
    UserRepository userRepository;

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") int id){
        Optional<User>userData = userRepository.findById(id);

        if(userData.isPresent()){
            return new ResponseEntity<>(userData.get(), HttpStatus.OK);
        } else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }}

        @PostMapping("/users")
        public ResponseEntity<User> createTutorial (@RequestBody User user) {
try{
    User u = new User (user.getName(),user.getAddress() ,user.getEmail(),user.getPhone(),user.getPinCode(),user.getPass(),user.getState() );
User user2 = userRepository.save(u);
return new ResponseEntity<>(user2, HttpStatus.CREATED);}
catch (Exception e) {

return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);}
    

    }

   @PutMapping("/User1/{id}")
    public ResponseEntity<User> updateUser (@PathVariable("id") int id , @RequestBody User user){
        Optional<User>userData = userRepository.findById(id);
         if(userData.isPresent()){
            User user3 = userData.get();
            user3.setAddress(user.getAddress());
            user3.setEmail(user.getEmail());
            user3.setName(user.getName());
            user3.setPhone(user.getPhone());
            user3.setPass(user.getPass());
            user3.setState(user.getState());
return new ResponseEntity<>(userRepository.save(user3),HttpStatus.OK);

         }
         else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
         }

     }
    }