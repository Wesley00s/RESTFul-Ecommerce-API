package org.wesley.ecommerce.application.exceptions;

public class AuthNotMatchesException extends RuntimeException {
    public AuthNotMatchesException() {
        super("Authentication failed, email and password does not match.");
    }
}