package com.example.test.controller;

import com.example.test.model.User;
import com.example.test.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
}
