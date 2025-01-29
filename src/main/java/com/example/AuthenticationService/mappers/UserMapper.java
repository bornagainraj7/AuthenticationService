package com.example.AuthenticationService.mappers;

import com.example.AuthenticationService.dtos.UserDto;
import com.example.AuthenticationService.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toUser(UserDto dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRoles());
        return user;
    }

    public UserDto toUserDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setRoles(user.getRole());
        return dto;
    }
}
