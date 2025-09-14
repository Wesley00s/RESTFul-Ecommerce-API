package org.wesley.ecommerce.application.config.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.wesley.ecommerce.application.domain.model.Users;
import org.wesley.ecommerce.application.service.UserService;

import java.util.ArrayList;

@Component
public class CustomUserDetailService implements UserDetailsService {
    private final UserService userService;

    public CustomUserDetailService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = this.userService.findByEmail(username);
        return new User(users.getEmail(), users.getPassword(), new ArrayList<>());
    }
}
