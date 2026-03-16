package com.backend.rentalBusiness.module.paymentAccount.service;

import com.backend.rentalBusiness.module.paymentAccount.dto.request.CreatePaymentAccountRequest;
import com.backend.rentalBusiness.module.paymentAccount.dto.response.PaymentAccountResponse;

import java.util.UUID;

public interface PaymentAccountService {

    PaymentAccountResponse createAccount(CreatePaymentAccountRequest request);

    PaymentAccountResponse getByBusiness(UUID businessId);

}