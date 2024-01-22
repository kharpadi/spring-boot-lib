package com.project.libraryapp.controllers;

import com.project.libraryapp.entities.User;
import com.project.libraryapp.repositories.UserRepository;
import com.project.libraryapp.requests.AuthResponseDto;
import com.project.libraryapp.requests.LoginRequest;
import com.project.libraryapp.requests.RegisterRequest;
import com.project.libraryapp.requests.UpdateUserDto;
import com.project.libraryapp.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.PrivateKey;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;
    public UserController(UserService userService){
        this.userService=userService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<User>retrieveAllUsers(){
        return userService.retrieveAllUsers();
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
        return userService.registerUser(registerRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequest loginRequest) {
        return userService.loginUser(loginRequest);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{userId}")
    public User getOneUser(@PathVariable long userId){
        return userService.getOneUser(userId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/{userId}")
    public User updateOneUser(@PathVariable long userId,@RequestBody UpdateUserDto newUser){
        return userService.updateOneUser(userId,newUser);
    }
    @PutMapping
    public ResponseEntity<?> updateAuthOneUser(@RequestBody UpdateUserDto newUser){
        return userService.updateAuthOneUser(newUser);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/{userId}")
    public void deleteUserById(@PathVariable long userId){
        userService.deleteById(userId);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAuthUser(){
        return userService.deleteAuthUser();
    }
}
