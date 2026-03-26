package com.backend.rentalBusiness.module.paymentAccount.service;

import com.backend.rentalBusiness.module.paymentAccount.dto.request.CreatePaymentAccountRequest;
import com.backend.rentalBusiness.module.paymentAccount.dto.request.CreateSubaccountRequest;
import com.backend.rentalBusiness.module.paymentAccount.dto.request.OnboardBusinessRequest;
import com.backend.rentalBusiness.module.paymentAccount.dto.request.ValidateBankRequest;
import com.backend.rentalBusiness.module.paymentAccount.dto.response.OnboardBusinessResponse;
import com.backend.rentalBusiness.module.paymentAccount.dto.response.PaymentAccountResponse;
import com.backend.rentalBusiness.module.paymentAccount.dto.response.ValidateBankResponse;

import java.util.UUID;

public interface PaymentAccountService {

    PaymentAccountResponse createAccount(CreatePaymentAccountRequest request);

    PaymentAccountResponse getByBusiness(UUID businessId);

    PaymentAccountResponse createSubaccount(CreateSubaccountRequest request);

    ValidateBankResponse validateBankAccount(ValidateBankRequest request);

    OnboardBusinessResponse onboardBusiness(OnboardBusinessRequest request);

}