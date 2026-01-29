package com.clouds3.userservice.repository;

import com.clouds3.userservice.entity.RoleEntity;
import com.clouds3.userservice.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByRoleName(Role role);
}
