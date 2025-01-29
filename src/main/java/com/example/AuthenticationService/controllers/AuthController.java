package com.example.AuthenticationService.controllers;


import com.example.AuthenticationService.dtos.LoginRequest;
import com.example.AuthenticationService.dtos.SignupRequest;
import com.example.AuthenticationService.dtos.UserDto;
import com.example.AuthenticationService.exceptions.PasswordMismatchException;
import com.example.AuthenticationService.exceptions.UserNotRegisteredException;
import com.example.AuthenticationService.mappers.UserMapper;
import com.example.AuthenticationService.models.Session;
import com.example.AuthenticationService.models.User;
import com.example.AuthenticationService.services.AuthService;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserMapper userMapper;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(@RequestBody SignupRequest signupRequest) {
        try {
            User user = authService.signup(signupRequest.getEmail(), signupRequest.getPassword());
            return new ResponseEntity<>(userMapper.toUserDto(user), HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginRequest loginRequest) {
        try {
            Session session = authService.login(loginRequest.getEmail(), loginRequest.getPassword());
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.add(HttpHeaders.SET_COOKIE, session.getToken());
            return new ResponseEntity<>(userMapper.toUserDto(session.getUser()), headers, HttpStatus.OK);
        } catch (UserNotRegisteredException e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (PasswordMismatchException e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }


}
