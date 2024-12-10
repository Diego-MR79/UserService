package com.capysoft.springcloud.mscv.users.controllers;

import com.capysoft.springcloud.mscv.users.entities.User;
import com.example.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.getUserById(id)
                .map(existingUser -> {
                    user.setId(id);
                    User updatedUser = userService.saveUser(user);
                    return ResponseEntity.ok(updatedUser);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User loginUser) {
        // Intentamos autenticar al usuario con el nombre de usuario
        User user = userService.authenticateUser(loginUser.getUsername());

        if (user == null) {
            // Si el usuario no existe, devolvemos un código 404 Not Found
            return ResponseEntity.status(404).build();
        }

        // Si el usuario existe pero la contraseña no coincide
        if (!user.getPassword().equals(loginUser.getPassword())) {
            return ResponseEntity.status(403).build();  // 403 Forbidden: contraseña incorrecta
        }

        // Si el usuario y la contraseña coinciden, devolvemos el usuario con un código 200 OK
        return ResponseEntity.ok(user);
    }
}
