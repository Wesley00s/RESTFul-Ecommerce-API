package org.wesley.ecommerce.application.service.implement;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.wesley.ecommerce.application.domain.model.Cart;
import org.wesley.ecommerce.application.domain.model.Users;
import org.wesley.ecommerce.application.service.UserService;

@Service
public class AuthenticationService {

    private final UserService userService;

    public AuthenticationService(UserService userService) {
        this.userService = userService;
    }

    public Users getAuthenticatedUser() {
        var authenticatedUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var authenticatedUserId = userService.findByEmail(authenticatedUser.getUsername()).getId();
        return userService.findById(authenticatedUserId);
    }

    public Cart getActiveCart(Users user) {
        var cart = user.getCart();
        if (cart == null) {
            throw new IllegalArgumentException("User does not have an active cart.");
        }
        return cart;
    }
}
