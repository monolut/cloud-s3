package com.clouds3.userservice.repository;

import com.clouds3.userservice.entity.UserEntity;
import com.clouds3.userservice.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    List<UserEntity> findByRole(Role role);
}
