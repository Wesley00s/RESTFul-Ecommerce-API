package org.wesley.ecommerce.application.service.implement;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.wesley.ecommerce.application.domain.model.Cart;
import org.wesley.ecommerce.application.domain.model.Users;
import org.wesley.ecommerce.application.domain.repository.UserRepository;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;

    public Users getAuthenticatedUser() {
        var authenticatedUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var authenticatedUserId = userRepository.findByEmail(authenticatedUser.getUsername())
                .orElseThrow(() -> new IllegalStateException("User is not authenticated"))
                .getId();
        return userRepository.findById(authenticatedUserId)
                .orElseThrow(() -> new IllegalStateException("User is not authenticated"));
    }

    public Cart getActiveCart(Users user) {
        var cart = user.getCart();
        if (cart == null) {
            throw new IllegalArgumentException("User does not have an active cart.");
        }
        return cart;
    }
}
