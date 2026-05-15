package com.backend.rentalBusiness.business.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateBusinessRequest(

        @NotBlank(message = "Business name is required")
        String businessName,

        String businessType,

        String contactPerson,

        @NotBlank(message = "Phone is required")
        @Size(min = 8, max = 20)
        String phone,

        @Email(message = "Invalid email format")
        String email,

        String address,

        String timezone,

        String currency

) {}
