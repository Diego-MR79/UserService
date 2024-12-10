package com.capysoft.springcloud.mscv.users.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.capysoft.springcloud.mscv.users.entities.User;

import java.util.Optional;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);

    List<User> findByRol(String rol);

    boolean existsByUsername(String username);
    
}
