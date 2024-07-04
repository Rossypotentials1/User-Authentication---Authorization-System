package com.rossypotentials.springsecurityjwt.repository;

import com.rossypotentials.springsecurityjwt.entity.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel, Integer> {

    Optional<UserModel> findByEmail(String email);
}
