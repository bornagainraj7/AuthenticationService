package com.example.AuthenticationService.models;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class User extends Base {
    private String email;
    private String password;
    private List<Role> role = new ArrayList<>();
}
