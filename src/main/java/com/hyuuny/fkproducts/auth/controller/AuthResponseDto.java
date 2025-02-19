package com.hyuuny.fkproducts.auth.controller;

import com.hyuuny.fkproducts.auth.sevice.UserTokenDto;
import com.hyuuny.fkproducts.users.doamin.Role;

import java.util.Set;
import java.util.stream.Collectors;

public class AuthResponseDto {

    public record UserWithTokenResponse(
            Long id,
            String email,
            Set<UserRoleResponse> roles,
            String accessToken
    ) {
        public UserWithTokenResponse(UserTokenDto.UserWithToken userWithToken) {
            this(
                    userWithToken.id(),
                    userWithToken.email(),
                    userWithToken.roles().stream()
                            .map(UserRoleResponse::new)
                            .collect(Collectors.toSet()),
                    userWithToken.accessToken()
            );
        }
    }

    public record UserRoleResponse(Role role) {
        public UserRoleResponse(UserTokenDto.UserRole userRole) {
            this(userRole.role());
        }
    }

}

