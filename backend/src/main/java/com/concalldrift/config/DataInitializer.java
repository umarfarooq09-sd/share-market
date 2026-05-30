package com.concalldrift.config;

import com.concalldrift.model.User;
import com.concalldrift.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Order(1)
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        createUserIfAbsent("DEMO1", "Tiger-Ocean-Marble-61", "USER");
        createUserIfAbsent("admin", "Admin@123", "ADMIN");
    }

    private void createUserIfAbsent(String username, String rawPassword, String role) {
        if (!userRepository.existsByUsername(username)) {
            User user = User.builder()
                    .username(username)
                    .password(passwordEncoder.encode(rawPassword))
                    .role(role)
                    .enabled(true)
                    .build();
            userRepository.save(user);
            log.info("Created user: {}", username);
        }
    }
}
