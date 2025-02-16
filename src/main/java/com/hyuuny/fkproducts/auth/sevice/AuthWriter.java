package com.hyuuny.fkproducts.auth.sevice;

import com.hyuuny.fkproducts.security.AuthUserDetails;
import com.hyuuny.fkproducts.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AuthWriter {

    private final JwtTokenProvider jwtTokenProvider;

    public String generateToken(AuthUserDetails userDetails) {
        return jwtTokenProvider.generateToken(
                userDetails.getUserId(),
                userDetails.getPassword(),
                userDetails.getRoles()
        );
    }

}
