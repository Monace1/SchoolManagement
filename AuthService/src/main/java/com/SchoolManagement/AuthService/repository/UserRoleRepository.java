package com.SchoolManagement.AuthService.repository;

import com.SchoolManagement.AuthService.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole,Long> {
}
