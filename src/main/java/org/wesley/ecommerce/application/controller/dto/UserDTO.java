package org.wesley.ecommerce.application.controller.dto;

import org.wesley.ecommerce.application.domain.enumeration.UserType;
import org.wesley.ecommerce.application.domain.model.Address;
import org.wesley.ecommerce.application.domain.model.Users;

import java.time.LocalDateTime;

public record UserDTO(
        String name,
        String email,
        String password,
        UserType userType,
        String street,
        String city,
        String state,
        String zip,
        LocalDateTime createdAt
) {

    public static UserDTO fromUser(Users users) {
        return new UserDTO(
                users.getName(),
                users.getEmail(),
                users.getPassword(),
                users.getUserType(),
                users.getAddress().getStreet(),
                users.getAddress().getCity(),
                users.getAddress().getState(),
                users.getAddress().getZip(),
                users.getCreatedAt()
        );
    }

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
        usr.setCreatedAt(createdAt);

        return usr;
    }
}
