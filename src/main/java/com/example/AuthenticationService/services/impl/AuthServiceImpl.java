package com.example.AuthenticationService.services.impl;

import com.example.AuthenticationService.models.*;
import com.example.AuthenticationService.repository.RoleRepository;
import com.example.AuthenticationService.repository.SessionRepository;
import io.jsonwebtoken.*;
import com.example.AuthenticationService.exceptions.PasswordMismatchException;
import com.example.AuthenticationService.exceptions.UserAlreadyExistsException;
import com.example.AuthenticationService.exceptions.UserNotRegisteredException;
import com.example.AuthenticationService.repository.UserRepository;
import com.example.AuthenticationService.services.AuthService;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private SecretKey secretKey;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public User signup(String email, String password) throws UserAlreadyExistsException {

        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            throw new UserAlreadyExistsException("User already signed-up, try logging in.");
        }

        Optional<Role> roleOptional = roleRepository.findRoleByValue("CUSTOMER");

        User user = new User();
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        // Role role = new Role();
        // role.setValue("CUSTOMER");

        List<Role> roles = new ArrayList<>();
        if (roleOptional.isPresent()) {
            roles.add(roleOptional.get());
        }
        user.setRole(roles);

        return userRepository.save(user);
    }

    @Override
    public Session login(String email, String password) throws PasswordMismatchException, UserNotRegisteredException {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new UserNotRegisteredException("Cannot find user, please register");
        }
        User user = userOptional.get();
        String hashPassword = user.getPassword();
        if (!bCryptPasswordEncoder.matches(password, hashPassword)) {
            throw new PasswordMismatchException("Email or password mismatch");
        }
        Map<String, Object> payload = new HashMap<>();
        Long now = System.currentTimeMillis();
        payload.put("iat", now);
        payload.put("exp", now + (1000 * 86400));
        payload.put("userId", user.getId());
        payload.put("scope", user.getRole());

        String token = Jwts.builder().claims(payload).signWith(secretKey).compact();

        Session session = new Session();
        session.setToken(token);
        session.setUser(user);
        session.setStatus(Status.ACTIVE);
        return sessionRepository.save(session);
    }

    @Override
    public Boolean validateToken(String token, Long userId) {
        Optional<Session> sessionOptional = sessionRepository.findByTokenAndUserId(token, userId);
        if (sessionOptional.isEmpty()) {
            return false;
        }
        Session session = sessionOptional.get();
        JwtParser parser = Jwts.parser().verifyWith(secretKey).build();
        Claims claims = parser.parseSignedClaims(token).getPayload();

        Long tokenExpiry = (Long) claims.get("exp");
        Long now = System.currentTimeMillis();

        if (session.getStatus() == Status.INACTIVE) {
            return false;
        }

        if (now >= (tokenExpiry + 10 * 1000 * 60)) {
            session.setStatus(Status.INACTIVE);
            sessionRepository.save(session);
            return false;
        }

        return true;
    }
}
