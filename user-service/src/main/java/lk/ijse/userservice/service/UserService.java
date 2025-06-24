package lk.ijse.userservice.service;


import lk.ijse.userservice.model.User;
import lk.ijse.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    public User register(User user) {
        return repository.save(user);
    }

    public Optional<User> login(String email, String password) {
        return repository.findByEmail(email)
                .filter(u -> u.getPassword().equals(password));
    }

    public Optional<User> getUser(Long id) {
        return repository.findById(id);
    }

    public User updateUser(Long id, User updatedUser) {
        User existingUser = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPassword(updatedUser.getPassword());
        existingUser.setRole(updatedUser.getRole());

        return repository.save(existingUser);
    }



    public boolean isUserRegistered(String email) {
        return repository.existsByEmail(email);
    }
}
