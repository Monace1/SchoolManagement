package com.SchoolManagement.AuthService.repository;

import com.SchoolManagement.AuthService.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Object> findByName(String roleName);


}
