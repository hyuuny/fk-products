package com.hyuuny.fkproducts.auth.sevice;

import com.hyuuny.fkproducts.security.AuthUserDetails;
import com.hyuuny.fkproducts.users.doamin.Role;

import java.util.Set;
import java.util.stream.Collectors;


public class UserTokenDto {

    public record UserWithToken(
            Long id,
            String email,
            Set<UserRole> roles,
            String accessToken
    ) {
        public UserWithToken(AuthUserDetails userDetails, String token) {
            this(
                    userDetails.getUserId(),
                    userDetails.getUsername(),
                    userDetails.getAuthorities().stream()
                            .map(role -> new UserRole(Role.valueOf(role.getAuthority())))
                            .collect(Collectors.toSet()),
                    token
            );
        }
    }

    public record UserRole(Role role) {
    }
}

