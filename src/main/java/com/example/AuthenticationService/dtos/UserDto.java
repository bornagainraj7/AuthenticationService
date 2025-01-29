package com.example.AuthenticationService.dtos;

import com.example.AuthenticationService.models.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserDto {
    private Long id;
    private String email;
    private List<Role> roles = new ArrayList<>();
}
