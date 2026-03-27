package com.backend.rentalBusiness.common.exception;

public class ResourceNotFoundException extends ApiException {

    public ResourceNotFoundException(String resource) {
        super(resource + " not found");
    }
}