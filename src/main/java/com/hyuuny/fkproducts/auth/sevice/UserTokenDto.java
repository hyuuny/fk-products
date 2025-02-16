package com.hyuuny.fkproducts.auth.sevice;

import com.hyuuny.fkproducts.security.AuthUserDetails;
import com.hyuuny.fkproducts.users.doamin.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;


public class UserTokenDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserWithToken {

        private Long id;

        private String email;

        private Set<UserRole> roles;

        private String accessToken;

        UserWithToken(AuthUserDetails userDetails, String token) {
            this.id = userDetails.getUserId();
            this.email = userDetails.getUsername();
            this.roles = userDetails.getAuthorities().stream()
                    .map(role -> new UserRole(Role.valueOf(role.getAuthority())))
                    .collect(Collectors.toSet());
            this.accessToken = token;
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserRole {

        private Role role;

    }
}
