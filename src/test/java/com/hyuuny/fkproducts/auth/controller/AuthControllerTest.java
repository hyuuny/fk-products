package com.hyuuny.fkproducts.auth.controller;

import com.hyuuny.fkproducts.BaseIntegrationTest;
import com.hyuuny.fkproducts.support.response.ResultType;
import com.hyuuny.fkproducts.users.doamin.Role;
import com.hyuuny.fkproducts.users.doamin.UserEntity;
import com.hyuuny.fkproducts.users.doamin.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest extends BaseIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }

    @DisplayName("로그인에 성공하면 액세스 토큰이 발급된다")
    @Test
    void auth() throws Exception {
        String password = "!@#secret123";
        UserEntity user = userRepository.save(
                UserEntity.builder()
                        .email("hyuuny@gmail.com")
                        .password(passwordEncoder.encode(password))
                        .roles(Set.of(Role.CUSTOMER))
                        .build()
        );

        AuthRequestDto.LoginRequest request = new AuthRequestDto.LoginRequest(user.getEmail(), password);

        mockMvc.perform(post("/api/v1/auth")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("result").value(ResultType.SUCCESS.name()))
                .andExpect(jsonPath("data.id").value(user.getId()))
                .andExpect(jsonPath("data.email").value(user.getEmail()))
                .andExpect(jsonPath("data.roles").exists())
                .andExpect(jsonPath("data.accessToken").exists())
        ;
    }
}