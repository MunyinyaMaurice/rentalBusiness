package com.backend.rentalBusiness.business.dto.request;

import jakarta.validation.constraints.Email;

public record UpdateBusinessRequest(

        String businessName,

        String businessType,

        String contactPerson,

        String phone,

        @Email
        String email,

        String address,

        String timezone,

        String currency

) {}
