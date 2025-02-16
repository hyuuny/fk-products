package com.hyuuny.fkproducts.security;

import com.hyuuny.fkproducts.users.doamin.UserEntity;
import com.hyuuny.fkproducts.users.doamin.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(username).orElseThrow(() ->
                new UsernameNotFoundException("user notFound email: " + username)
        );
        return new AuthUserDetails(user);
    }
}
