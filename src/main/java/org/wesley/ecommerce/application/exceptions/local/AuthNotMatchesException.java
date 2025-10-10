package org.wesley.ecommerce.application.exceptions.local;

public class AuthNotMatchesException extends RuntimeException {
    public AuthNotMatchesException() {
        super("Authentication failed, email and password does not match.");
    }
}