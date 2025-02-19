package com.hyuuny.fkproducts.config;

import com.hyuuny.fkproducts.users.doamin.Role;
import com.hyuuny.fkproducts.users.doamin.UserEntity;
import com.hyuuny.fkproducts.users.doamin.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

import static com.hyuuny.fkproducts.BaseIntegrationTest.ADMIN_EMAIL;
import static com.hyuuny.fkproducts.BaseIntegrationTest.ADMIN_PASSWORD;

@RequiredArgsConstructor
@Component
public class TestApplicationRunner implements ApplicationRunner {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        userRepository.save(
                UserEntity.builder()
                        .email(ADMIN_EMAIL)
                        .password(passwordEncoder.encode(ADMIN_PASSWORD))
                        .roles(Set.of(Role.ADMIN, Role.CUSTOMER))
                        .build()
        );
    }
}
