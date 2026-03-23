package com.backend.rentalBusiness.auth.exceptions;

public class TenantNotFoundException extends RuntimeException {
    public TenantNotFoundException(String message) {
        super(message);
    }
}

