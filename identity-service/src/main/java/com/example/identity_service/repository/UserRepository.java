package com.example.identity_service.repository;

import com.example.identity_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    // su ki dieu cua spring jpa khi chi can khai bao ntn la no tu dong bien thanh ham check exist username hay k
    boolean existsByUsername(String username);

    // tuong tu nhu mapstruct no se tu dong gen code
    Optional<User> findByUsername(String username);

}
