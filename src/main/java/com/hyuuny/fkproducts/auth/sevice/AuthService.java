package com.hyuuny.fkproducts.auth.sevice;

import com.hyuuny.fkproducts.security.AuthUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final AuthReader authReader;
    private final AuthWriter authWriter;

    public UserTokenDto.UserWithToken auth(String email, String password) {
        Authentication authentication = authReader.getAuthentication(email, password);
        AuthUserDetails authUserDetails = (AuthUserDetails) authentication.getPrincipal();
        String accessToken = authWriter.generateToken(authUserDetails);
        return new UserTokenDto.UserWithToken(authUserDetails, accessToken);
    }

}
