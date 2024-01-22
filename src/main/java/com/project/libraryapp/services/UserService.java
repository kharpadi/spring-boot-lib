package com.project.libraryapp.services;

import com.project.libraryapp.entities.Role;
import com.project.libraryapp.entities.User;
import com.project.libraryapp.repositories.RoleRepository;
import com.project.libraryapp.repositories.UserRepository;
import com.project.libraryapp.requests.AuthResponseDto;
import com.project.libraryapp.requests.LoginRequest;
import com.project.libraryapp.requests.RegisterRequest;
import com.project.libraryapp.requests.UpdateUserDto;
import com.project.libraryapp.security.JWTGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private JWTGenerator jwtGenerator;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;

    public UserService(AuthenticationManager authenticationManager,PasswordEncoder passwordEncoder,JWTGenerator jwtGenerator,RoleRepository roleRepository,UserRepository userRepository) {
        this.authenticationManager=authenticationManager;
        this.passwordEncoder=passwordEncoder;
        this.roleRepository=roleRepository;
        this.userRepository = userRepository;
        this.jwtGenerator=jwtGenerator;
    }


    public List<User> retrieveAllUsers() {
        return userRepository.findAll();
    }

    public User createUser(User newUser) {
        Role roles=roleRepository.findByName("USER").get();
        newUser.setRoles(Collections.singletonList(roles));
        return userRepository.save(newUser);
    }

    public User getOneUser(long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public User updateOneUser(long userId, UpdateUserDto newUser) {
        Optional<User> user=userRepository.findById(userId);
        if (user.isPresent()){
            User foundUser=user.get();
            foundUser.setUsername(newUser.getUsername());
            foundUser.setPassword(newUser.getPassword());
            foundUser.setRoles(newUser.getRoles());
            userRepository.save(foundUser);
            return foundUser;
        }else {
            return null;
        }
    }

    public void deleteById(long userId) {
        userRepository.deleteById(userId);
    }

    public ResponseEntity<String> registerUser(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            return new ResponseEntity<>("Username is taken", HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        Role role = roleRepository.findByName("USER").get();
        user.setRoles(Collections.singletonList(role));

        userRepository.save(user);
        return new ResponseEntity<>("User is registered!!", HttpStatus.OK);
    }

    public ResponseEntity<AuthResponseDto> loginUser(LoginRequest loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsername(),
                        loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtGenerator.generateToken(authentication);
        return new ResponseEntity<>(new AuthResponseDto(token), HttpStatus.OK);
    }

    public User findByUsername(String username){
        return userRepository.findByUsername(username).orElseThrow();
    }

    public ResponseEntity<?> updateAuthOneUser(UpdateUserDto newUser) {
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()){
            String username=authentication.getName();
            User user =userRepository.findByUsername(username).orElseThrow();
            user.setUsername(newUser.getUsername());
            user.setPassword(passwordEncoder.encode(newUser.getPassword()));
            user.setRoles(newUser.getRoles());
            userRepository.save(user);
            return ResponseEntity.ok("Everything is updated");
        }
        return new ResponseEntity<>("Something went wrong!",HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> deleteAuthUser() {
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()){
            String username=authentication.getName();
            User user=userRepository.findByUsername(username).orElseThrow();
            userRepository.delete(user);
            return ResponseEntity.ok("Your Account has been deleted");
        }
        else
            return new ResponseEntity<>("Something went wrong!!",HttpStatus.BAD_REQUEST);
    }
}