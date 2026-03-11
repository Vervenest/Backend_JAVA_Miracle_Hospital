package com.miracle.service;

import com.miracle.dto.AuthenticationRequest;
import com.miracle.dto.AuthenticationResponse;
import com.miracle.dto.UserDto;
import com.miracle.model.User;
import com.miracle.repository.UserRepository;
import com.miracle.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthenticationResponse register(UserDto userDto) {
        if (userRepository.existsByUserName(userDto.getUserName())) {
            return AuthenticationResponse.builder()
                    .success(false)
                    .message("Username already exists")
                    .build();
        }

        if (userRepository.existsByEmail(userDto.getEmail())) {
            return AuthenticationResponse.builder()
                    .success(false)
                    .message("Email already exists")
                    .build();
        }

        User user = User.builder()
                .userName(userDto.getUserName())
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .userRole(User.UserRole.valueOf(userDto.getRole()))
                .isActive(true)
                .build();

        User savedUser = userRepository.save(user);

        String token = jwtUtil.generateToken(savedUser.getUserName(), savedUser.getUserRole().toString());

        return AuthenticationResponse.builder()
                .success(true)
                .token(token)
                .userId(savedUser.getUserId())
                .userName(savedUser.getUserName())
                .email(savedUser.getEmail())
                .role(savedUser.getUserRole().toString())
                .message("User registered successfully")
                .build();
    }

    public AuthenticationResponse login(AuthenticationRequest request) {
        var user = userRepository.findByUserName(request.getUserName())
                .orElse(null);

        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return AuthenticationResponse.builder()
                    .success(false)
                    .message("Invalid username or password")
                    .build();
        }

        if (!user.getIsActive()) {
            return AuthenticationResponse.builder()
                    .success(false)
                    .message("User account is inactive")
                    .build();
        }

        String token = jwtUtil.generateToken(user.getUserName(), user.getUserRole().toString());

        return AuthenticationResponse.builder()
                .success(true)
                .token(token)
                .userId(user.getUserId())
                .userName(user.getUserName())
                .email(user.getEmail())
                .role(user.getUserRole().toString())
                .message("Login successful")
                .build();
    }

    public User getUserByUserName(String userName) {
        return userRepository.findByUserName(userName).orElse(null);
    }
}
