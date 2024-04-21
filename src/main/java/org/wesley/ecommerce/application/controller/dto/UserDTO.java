package org.wesley.ecommerce.application.controller.dto;

import org.wesley.ecommerce.application.domain.enumeration.UserType;
import org.wesley.ecommerce.application.domain.model.Address;
import org.wesley.ecommerce.application.domain.model.User;

public record UserDTO(
        String name,
        String email,
        String password,
        UserType userType,
        String street,
        String city,
        String state,
        String zip
) {

    public static UserDTO fromUser(User user) {
        return new UserDTO(
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getUserType(),
                user.getAddress().getStreet(),
                user.getAddress().getCity(),
                user.getAddress().getState(),
                user.getAddress().getZip()
        );
    }

    public User from() {
        var address = new Address();
        address.setStreet(street());
        address.setCity(city());
        address.setState(state());
        address.setZip(zip());

        var usr = new User();
        usr.setName(name());
        usr.setEmail(email());
        usr.setPassword(password());
        usr.setUserType(userType());
        usr.setAddress(address);
        return usr;
    }
}
