package com.ul.customerprofile.exception;

public class EmailExistsException extends RuntimeException {
    public EmailExistsException(String email) {
        super("Email already registered: " + email);
    }
}
