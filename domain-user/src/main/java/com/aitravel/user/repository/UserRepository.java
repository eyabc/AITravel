package com.aitravel.user.repository;

import com.aitravel.user.entity.User;
import com.aitravel.user.entity.value.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(Email email);
}
