package com.capysoft.springcloud.mscv.users.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capysoft.springcloud.mscv.users.entities.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
