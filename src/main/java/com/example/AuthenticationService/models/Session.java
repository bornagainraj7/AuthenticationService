package com.example.AuthenticationService.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Session extends Base {
    private String token;
    @ManyToOne
    private User user;
}
