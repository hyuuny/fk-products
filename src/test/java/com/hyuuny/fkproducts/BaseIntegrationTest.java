package com.hyuuny.fkproducts;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyuuny.fkproducts.auth.controller.AuthRequestDto;
import com.hyuuny.fkproducts.auth.controller.AuthResponseDto;
import com.hyuuny.fkproducts.support.response.ApiResponse;
import com.hyuuny.fkproducts.users.doamin.UserRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Objects;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseIntegrationTest {

    public final static String ADMIN_EMAIL = "admin@gmail.com";

    public final static String ADMIN_PASSWORD = "!@#adminsecret123";

    public final static String CUSTOMER_EMAIL = "customer@gmail.com";

    public final static String CUSTOMER_PASSWORD = "!@#customersecret123";

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    protected String getBearerToken(final String email, final String password) throws Exception {
        String accessToken = getAccessToken(email, password);
        return "Bearer " + accessToken;
    }

    private String getAccessToken(final String email, final String password) throws Exception {
        ResultActions perform = this.mockMvc.perform(post("/api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(this.objectMapper.writeValueAsString(new AuthRequestDto.LoginRequest(email, password))));

        String responseBody = perform.andReturn().getResponse().getContentAsString();
        ApiResponse<AuthResponseDto.UserWithTokenResponse> response = objectMapper.readValue(responseBody, new TypeReference<>() {
        });
        return response.getData().getAccessToken();
    }

    protected void deleteAllUser() {
        userRepository.findAll().stream()
                .filter(user -> !Objects.equals(user.getEmail(), ADMIN_EMAIL))
                .filter(user -> !Objects.equals(user.getEmail(), CUSTOMER_EMAIL))
                .forEach(userRepository::delete);
    }

}
