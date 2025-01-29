package com.example.AuthenticationService.services;

import com.example.AuthenticationService.exceptions.PasswordMismatchException;
import com.example.AuthenticationService.exceptions.UserAlreadyExistsException;
import com.example.AuthenticationService.exceptions.UserNotRegisteredException;
import com.example.AuthenticationService.models.Session;
import com.example.AuthenticationService.models.User;
import org.antlr.v4.runtime.misc.Pair;

public interface AuthService {

    User signup(String email, String password) throws UserAlreadyExistsException;

    Session login(String email, String password) throws PasswordMismatchException, UserNotRegisteredException;

    Boolean validateToken(String token, Long userId);

}
