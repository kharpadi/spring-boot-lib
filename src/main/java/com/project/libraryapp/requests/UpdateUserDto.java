package com.project.libraryapp.requests;

import com.project.libraryapp.entities.Role;
import lombok.Data;

import java.util.List;

@Data
public class UpdateUserDto {
    private String username;
    private String password;
    private List<Role> roles;
}
