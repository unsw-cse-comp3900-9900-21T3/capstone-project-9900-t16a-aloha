package com.example.test.controller;

import java.util.Optional;

import com.example.test.model.User;
import com.example.test.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path="/test")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping(path = "/all")
    public @ResponseBody Iterable<User> getAllUser() {
        return userRepository.findAll();
    }

    @PostMapping(path = "/add")
    public @ResponseBody String addNewUser(@RequestParam String email,@RequestParam String password,
                                           @RequestParam String firstname, @RequestParam String lastname
                                           ) {
        User u = new User();
        u.setEmail(email);
        u.setPassword(password);
        u.setFirstName(firstname);
        u.setLastName(lastname);
        u.setTag(0);
        u.setId(0);
        userRepository.save(u);
        return "saved";
    }

    // update the customer account info such as firstname, lastname, email, telephone
    @PostMapping(path = "/user/{id}/account")
    public @ResponseBody User updateUserAccount(@PathVariable Integer id,  @RequestBody User user) {
        Optional<User> userToUpdateOption = this.userRepository.findById(id);
        // check if the user existed in database
        if (!userToUpdateOption.isPresent()) {
            return null;
        }
        // optional get id from user
        
        User userToUpdate = userToUpdateOption.get();
    
        if (user.getFirstName() != null) {
            userToUpdate.setFirstName(user.getFirstName());
        }
        if (user.getLastName() != null) {
            userToUpdate.setLastName(user.getLastName());
        }
        if (user.getEmail() != null) {
            userToUpdate.setEmail(user.getEmail());
        }
        if (user.getTelephone() != null) {
            userToUpdate.setTelephone(user.getTelephone());
        }
        
        User updatedUser = this.userRepository.save(userToUpdate);
        return updatedUser;
    }

    // update the customer address info such as 
    @PostMapping(path = "/user/{id}/address") 
    public @ResponseBody User updateUserAddress(@PathVariable("id") Integer id, @RequestBody User user) {
        Optional<User> userToUpdateOption = this.userRepository.findById(id);
        // check if the user existed in database
        if (!userToUpdateOption.isPresent()) {
            return null;
        }

        User userToUpdate = userToUpdateOption.get();

        if (user.getFirstName() != null) {
            userToUpdate.setFirstName(user.getFirstName());
        }
        if (user.getLastName() != null) {
            userToUpdate.setLastName(user.getLastName());
        }
        if (user.getTelephone() != null) {
            userToUpdate.setTelephone(user.getTelephone());
        }
        if (user.getCity() != null) {
            userToUpdate.setCity(user.getCity());
        }
        if (user.getPostcode() != null) {
            userToUpdate.setPostcode(user.getPostcode());
        }
        if (user.getState() != null) {
            userToUpdate.setState(user.getState());
        }
        if (user.getStreet() != null) {
            userToUpdate.setStreet(user.getStreet());
        }

        User updatedUser = this.userRepository.save(userToUpdate);
        return updatedUser;
    }

}
