package com.example.AuthenticationService.repository;

import com.example.AuthenticationService.models.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    Session save(Session session);

    Optional<Session> findByTokenAndUserId(String token, Long userId);
}