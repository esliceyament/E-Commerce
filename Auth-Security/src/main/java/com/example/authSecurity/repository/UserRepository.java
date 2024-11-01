package com.example.authSecurity.repository;

import com.example.authSecurity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    boolean existsByUsername(String userName);
    boolean existsByPhone(String phone);

    Optional<User> findFirstByStatusTrueOrderByOrderNumberDesc();
}
