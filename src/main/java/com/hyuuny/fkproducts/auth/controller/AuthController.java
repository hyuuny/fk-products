package com.hyuuny.fkproducts.auth.controller;

import com.hyuuny.fkproducts.auth.sevice.AuthService;
import com.hyuuny.fkproducts.auth.sevice.UserTokenDto;
import com.hyuuny.fkproducts.support.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping
    public ApiResponse<AuthResponseDto.UserWithTokenResponse> auth(
            @RequestBody @Valid AuthRequestDto.LoginRequest request
    ) {
        UserTokenDto.UserWithToken userWithToken = authService.auth(request.email(), request.password());
        return ApiResponse.success(new AuthResponseDto.UserWithTokenResponse(userWithToken));
    }

}
