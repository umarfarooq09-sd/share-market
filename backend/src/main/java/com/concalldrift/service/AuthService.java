package com.concalldrift.service;

import com.concalldrift.dto.LoginRequest;
import com.concalldrift.dto.LoginResponse;
import com.concalldrift.model.User;
import com.concalldrift.repository.UserRepository;
import com.concalldrift.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        String token = jwtTokenProvider.generateToken(authentication);
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new LoginResponse(token, user.getUsername(), user.getRole());
    }
}
