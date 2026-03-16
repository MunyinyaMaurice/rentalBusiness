package com.backend.rentalBusiness.module.paymentAccount.controller;

import com.backend.rentalBusiness.module.paymentAccount.dto.request.CreatePaymentAccountRequest;
import com.backend.rentalBusiness.module.paymentAccount.dto.response.PaymentAccountResponse;
import com.backend.rentalBusiness.module.paymentAccount.service.PaymentAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/payment-accounts")
@RequiredArgsConstructor
public class PaymentAccountController {

    private final PaymentAccountService service;

    @PostMapping
    public PaymentAccountResponse createAccount(
            @RequestBody CreatePaymentAccountRequest request) {

        return service.createAccount(request);
    }

    @GetMapping("/business/{businessId}")
    public PaymentAccountResponse getByBusiness(
            @PathVariable UUID businessId) {

        return service.getByBusiness(businessId);
    }
}