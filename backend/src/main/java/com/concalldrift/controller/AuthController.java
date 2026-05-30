package com.concalldrift.controller;

import com.concalldrift.dto.ApiResponse;
import com.concalldrift.dto.LoginRequest;
import com.concalldrift.dto.LoginResponse;
import com.concalldrift.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/verify")
    public ResponseEntity<ApiResponse<String>> verify() {
        return ResponseEntity.ok(ApiResponse.ok("Token is valid"));
    }
}
