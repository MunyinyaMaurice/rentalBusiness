package com.backend.rentalBusiness.auth.exceptions;

public class TenantAlreadyExistsException extends RuntimeException {
    public TenantAlreadyExistsException(String message) {
        super(message);
    }
}

