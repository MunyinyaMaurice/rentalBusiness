package com.backend.rentalBusiness.paymentAccount.controller;

import com.backend.rentalBusiness.paymentAccount.dto.request.CreatePaymentAccountRequest;
import com.backend.rentalBusiness.paymentAccount.dto.request.CreateSubaccountRequest;
import com.backend.rentalBusiness.paymentAccount.dto.request.OnboardBusinessRequest;
import com.backend.rentalBusiness.paymentAccount.dto.request.ValidateBankRequest;
import com.backend.rentalBusiness.paymentAccount.dto.response.OnboardBusinessResponse;
import com.backend.rentalBusiness.paymentAccount.dto.response.PaymentAccountResponse;
import com.backend.rentalBusiness.paymentAccount.dto.response.ValidateBankResponse;
import com.backend.rentalBusiness.paymentAccount.service.PaymentAccountService;
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

    @PostMapping("/subaccount")
public PaymentAccountResponse createSubaccount(
        @RequestBody CreateSubaccountRequest request) {

    return service.createSubaccount(request);
}
@PostMapping("/validate-bank")
    public ValidateBankResponse validateBank(
            @RequestBody ValidateBankRequest request) {

        return service.validateBankAccount(request);
    }

    @PostMapping("/onboard")
public OnboardBusinessResponse onboardBusiness(
        @RequestBody OnboardBusinessRequest request) {

    return service.onboardBusiness(request);
}
}