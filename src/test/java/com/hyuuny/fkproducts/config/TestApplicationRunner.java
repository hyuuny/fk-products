package com.hyuuny.fkproducts.config;

import com.hyuuny.fkproducts.users.doamin.Role;
import com.hyuuny.fkproducts.users.doamin.UserEntity;
import com.hyuuny.fkproducts.users.doamin.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;

import static com.hyuuny.fkproducts.BaseIntegrationTest.*;

@RequiredArgsConstructor
@Component
public class TestApplicationRunner implements ApplicationRunner {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        userRepository.saveAll(
                Arrays.asList(
                        UserEntity.builder()
                                .email(ADMIN_EMAIL)
                                .password(passwordEncoder.encode(ADMIN_PASSWORD))
                                .roles(Set.of(Role.ADMIN, Role.CUSTOMER))
                                .build(),
                        UserEntity.builder()
                                .email(CUSTOMER_EMAIL)
                                .password(passwordEncoder.encode(CUSTOMER_PASSWORD))
                                .roles(Set.of(Role.CUSTOMER))
                                .build()
                )

        );
    }
}
