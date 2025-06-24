package lk.ijse.userservice.controller;


import lk.ijse.userservice.model.User;
import lk.ijse.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return service.register(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        Optional<User> loggedIn = service.login(user.getEmail(), user.getPassword());
        return loggedIn.isPresent() ? "Login Successful" : "Invalid Credentials";
    }

    @GetMapping("/{id}")
    public Optional<User> getUser(@PathVariable Long id) {
        return service.getUser(id);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        return service.updateUser(id, updatedUser);
    }
    @GetMapping("/email/{email}")
    public ResponseEntity<Boolean> isUserRegistered(@PathVariable String email) {
        boolean exists = service.isUserRegistered(email);
        return ResponseEntity.ok(exists);
    }


}
