package org.wesley.ecommerce.application.controller.dto.request;

import org.wesley.ecommerce.application.domain.enumeration.UserType;
import org.wesley.ecommerce.application.domain.model.Address;
import org.wesley.ecommerce.application.domain.model.Users;

import java.time.LocalDateTime;

public record UserRequest(
        String name,
        String email,
        String password,
        UserType userType,
        String street,
        String city,
        String state,
        String zip
) {

    public Users from() {
        var address = new Address();
        address.setStreet(street());
        address.setCity(city());
        address.setState(state());
        address.setZip(zip());

        var usr = new Users();
        usr.setName(name());
        usr.setEmail(email());
        usr.setPassword(password());
        usr.setUserType(userType());
        usr.setAddress(address);
        usr.setCreatedAt(LocalDateTime.now());

        return usr;
    }
}
