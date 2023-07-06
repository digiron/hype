package com.digiron.hype.data.repository;

import com.digiron.hype.data.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, String> {
    public User getUserByUsername(String username);

    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

}
