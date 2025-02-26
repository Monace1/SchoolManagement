package com.SchoolManagement.AuthService.controller;

import com.SchoolManagement.AuthService.dto.ApiResponse;
import com.SchoolManagement.AuthService.dto.Logindto;
import com.SchoolManagement.AuthService.dto.RoleAssignmentDto;
import com.SchoolManagement.AuthService.dto.UserRegistrationDto;
import com.SchoolManagement.AuthService.model.Role;
import com.SchoolManagement.AuthService.model.User;
import com.SchoolManagement.AuthService.model.UserRole;
import com.SchoolManagement.AuthService.repository.RoleRepository;
import com.SchoolManagement.AuthService.repository.UserRepository;
import com.SchoolManagement.AuthService.repository.UserRoleRepository;
import com.SchoolManagement.AuthService.security.JwtUtil;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationDto userRegistrationDto) {
        if (userRepository.findByUsername(userRegistrationDto.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(null, "Username already exists", HttpStatus.BAD_REQUEST.value()));
        }

        User user = new User();
        user.setUsername(userRegistrationDto.getUsername());
        user.setFirstname(userRegistrationDto.getFirstname());
        user.setLastname(userRegistrationDto.getLastname());
        user.setEmail(userRegistrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword()));

        userRepository.save(user);

        Role userRole = (Role) roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new EntityNotFoundException("Default role ROLE_USER not found"));

        UserRole userRoleMapping = new UserRole(user, userRole);
        userRoleRepository.save(userRoleMapping);

        return ResponseEntity.ok().body(new ApiResponse(null, "Registration successful", HttpStatus.OK.value()));
    }

    @PostMapping("/assign-role")
    @PreAuthorize("hasAuthority('ROLE_SUPERADMIN')")
    public ResponseEntity<?> assignRole(@RequestBody RoleAssignmentDto roleAssignmentDto) {
        User user = userRepository.findById(roleAssignmentDto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Role role = (Role) roleRepository.findByName("ROLE_" + roleAssignmentDto.getRoleName().toUpperCase())
                .orElseThrow(() -> new EntityNotFoundException("Role not found"));

        boolean roleExists = user.getUserRoles().stream()
                .anyMatch(userRole -> userRole.getRole().equals(role));

        if (roleExists) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(null, "User already has this role", HttpStatus.BAD_REQUEST.value()));
        }

        UserRole userRole = new UserRole(user, role);
        userRoleRepository.save(userRole);

        return ResponseEntity.ok().body(new ApiResponse(null, "Role assigned successfully", HttpStatus.OK.value()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody Logindto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Set<String> roles = user.getUserRoles().stream()
                .map(ur -> ur.getRole().getName())
                .collect(Collectors.toSet());

        String token = jwtUtil.generateToken(request.getUsername(), (List<String>) roles);
        return ResponseEntity.ok().body(new ApiResponse(token, "Login successful", HttpStatus.OK.value()));
    }
}
