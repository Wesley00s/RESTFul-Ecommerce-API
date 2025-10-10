package org.wesley.ecommerce.application.service.implement;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.wesley.ecommerce.application.config.security.TokenService;
import org.wesley.ecommerce.application.api.v1.controller.dto.response.AuthResponse;
import org.wesley.ecommerce.application.api.v1.controller.dto.request.UserRequest;
import org.wesley.ecommerce.application.api.v1.controller.dto.response.LoginResponse;
import org.wesley.ecommerce.application.domain.model.Cart;
import org.wesley.ecommerce.application.domain.model.Users;
import org.wesley.ecommerce.application.domain.repository.UserRepository;
import org.wesley.ecommerce.application.exceptions.local.AuthNotMatchesException;
import org.wesley.ecommerce.application.service.CartService;
import org.wesley.ecommerce.application.service.UserService;

import java.time.Duration;
import java.util.NoSuchElementException;
import java.util.UUID;


@Service
@AllArgsConstructor
public class UserServiceImplement implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CartService cartService;

    @Override
    public Users findById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + id));
    }

    @Override
    public Page<Users> findAll(Integer page, Integer pageSize) {
        return userRepository.findAll(PageRequest.of(page, pageSize));
    }

    @Override
    public Users create(UserRequest userRequest) {
        if (userRepository.findByEmail(userRequest.email()).isPresent()) {
            throw new IllegalArgumentException("E-mail already in use");
        }

        var user = new Users();
        user.setName(userRequest.name());
        user.setEmail(userRequest.email());
        user.setPassword(bCryptPasswordEncoder.encode(userRequest.password()));
        user.setUserType(userRequest.userType());
        user.setAddress(userRequest.from().getAddress());

        var userSaved = userRepository.save(user);

        var cart = new Cart();
        cart.setUser(userSaved);
        cartService.create(cart);

        return userSaved;
    }

    @Override
    public Users update(UUID id, UserRequest userRequest) {
        Users existingUser = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        if (userRequest.name() != null) existingUser.setName(userRequest.name());
        if (userRequest.email() != null) existingUser.setEmail(userRequest.email());
        if (userRequest.password() != null) existingUser.setPassword(userRequest.password());
        if (userRequest.userType() != null) existingUser.setUserType(userRequest.userType());
        if (userRequest.street() != null) existingUser.getAddress().setStreet(userRequest.street());
        if (userRequest.city() != null) existingUser.getAddress().setCity(userRequest.city());
        if (userRequest.state() != null) existingUser.getAddress().setState(userRequest.state());
        if (userRequest.zip() != null) existingUser.getAddress().setZip(userRequest.zip());

        return userRepository.save(existingUser);
    }

    @Override
    public void delete(Users users) {
        if (userRepository.existsById(users.getId())) {
            userRepository.deleteById(users.getId());
        } else {
            throw new NoSuchElementException("User with id " + users.getId() + " not found for delete");
        }
    }

    @Override
    public Users findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    @Override
    public LoginResponse authenticate(String email, String password, HttpServletResponse response) {
        var user = findByEmail(email);
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new AuthNotMatchesException();
        }
        var token = tokenService.generateToken(user.getEmail(), user.getId(), user.getName());

        var result = new AuthResponse(token, user.getId(), user.getName(), user.getEmail(), user.getUserType());
        addJwtCookieToResponse(result, response);
        return LoginResponse.of(result);
    }

    private static void addJwtCookieToResponse(AuthResponse authResult, HttpServletResponse response) {
        String token = authResult.token();

        ResponseCookie cookie = ResponseCookie.from("token", token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("None")
                .maxAge(Duration.ofDays(1))
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}