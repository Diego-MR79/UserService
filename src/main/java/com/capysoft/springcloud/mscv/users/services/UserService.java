package com.capysoft.springcloud.mscv.users.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.dao.EmptyResultDataAccessException;

import com.capysoft.springcloud.mscv.users.entities.User;
import com.capysoft.springcloud.mscv.users.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User saveUser(User user) {
        if (user.getId() == null) {
            return userRepository.save(user);
        }
    
        Optional<User> existingUser = userRepository.findById(user.getId());
        if (existingUser.isPresent()) {
            return userRepository.save(user);
        }
        throw new RuntimeException("User with id " + user.getId() + " not found");
    }

    public void deleteUser(Long id) {
        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeException("User with id " + id + " not found");
        }
    }
    
    // Método de autenticación (solo busca por nombre de usuario)
    public User authenticateUser(String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);  // Buscar usuario por nombre
        return userOpt.orElse(null);  // Si no encuentra el usuario, retorna null
    }
}
