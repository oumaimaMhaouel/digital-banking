package com.example.digitalbanking.dtos;

import lombok.Data;

import java.util.List;

@Data
public class AppUserDTO {
    private Long id;
    private String username;
    private List<String> roles;
}
