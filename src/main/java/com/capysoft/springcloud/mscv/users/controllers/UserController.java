package com.capysoft.springcloud.mscv.users.controllers;

import com.capysoft.springcloud.mscv.users.entities.User;
import com.capysoft.springcloud.mscv.users.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Para encriptar contraseñas
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(user -> {
                    user.setPassword(null);  // Removemos la contraseña antes de devolver el usuario
                    return ResponseEntity.ok(user);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        // Encriptamos la contraseña antes de guardarla
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        return userService.saveUser(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.getUserById(id)
                .map(existingUser -> {
                    user.setId(id);
                    // Si la contraseña se ha actualizado, la encriptamos
                    if (user.getPassword() != null && !user.getPassword().equals(existingUser.getPassword())) {
                        user.setPassword(passwordEncoder.encode(user.getPassword()));
                    }
                    User updatedUser = userService.saveUser(user);
                    updatedUser.setPassword(null);  // Aseguramos que no se devuelva la contraseña
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

        // Comparamos las contraseñas utilizando BCrypt
        if (!passwordEncoder.matches(loginUser.getPassword(), user.getPassword())) {
            return ResponseEntity.status(403).build();  // 403 Forbidden: contraseña incorrecta
        }

        // Si el usuario y la contraseña coinciden, devolvemos el usuario con un código 200 OK
        user.setPassword(null);  // Aseguramos que no se devuelva la contraseña
        return ResponseEntity.ok(user);
    }
}
