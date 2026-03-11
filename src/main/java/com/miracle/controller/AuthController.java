package com.miracle.controller;

import com.miracle.dto.AuthenticationRequest;
import com.miracle.dto.AuthenticationResponse;
import com.miracle.dto.UserDto;
import com.miracle.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody UserDto userDto) {
        log.info("Registering new user: {}", userDto.getUserName());
        AuthenticationResponse response = authService.register(userDto);
        return response.isSuccess() 
            ? ResponseEntity.status(HttpStatus.CREATED).body(response)
            : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        log.info("User login attempt: {}", request.getUserName());
        AuthenticationResponse response = authService.login(request);
        return response.isSuccess()
            ? ResponseEntity.ok(response)
            : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        log.info("User logout");
        return ResponseEntity.ok("Logout successful");
    }
}
