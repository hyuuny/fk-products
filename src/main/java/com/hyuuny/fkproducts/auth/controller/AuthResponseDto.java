package com.hyuuny.fkproducts.auth.controller;

import com.hyuuny.fkproducts.auth.sevice.UserTokenDto;
import com.hyuuny.fkproducts.users.doamin.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

public class AuthResponseDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserWithTokenResponse {

        private Long id;

        private String email;

        private Set<UserRoleResponse> roles;

        private String accessToken;

        UserWithTokenResponse(UserTokenDto.UserWithToken userWithToken) {
            this.id = userWithToken.getId();
            this.email = userWithToken.getEmail();
            this.roles = userWithToken.getRoles().stream()
                    .map(UserRoleResponse::new)
                    .collect(Collectors.toSet());
            this.accessToken = userWithToken.getAccessToken();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserRoleResponse {

        private Role role;

        UserRoleResponse(UserTokenDto.UserRole role) {
            this.role = role.getRole();
        }
    }

}
