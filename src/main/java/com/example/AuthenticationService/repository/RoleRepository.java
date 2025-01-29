package com.example.AuthenticationService.repository;

import com.example.AuthenticationService.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query("SELECT r from Role r where r.value=:role AND r.status='ACTIVE'")
    Optional<Role> findRoleByValue(String role);

}
