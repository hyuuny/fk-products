package com.hyuuny.fkproducts.auth.controller;

import jakarta.validation.constraints.NotNull;

public class AuthRequestDto {

    public record LoginRequest(
            @NotNull String email,
            @NotNull String password
    ) {
    }
}

