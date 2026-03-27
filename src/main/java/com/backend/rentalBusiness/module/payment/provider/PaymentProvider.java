package com.backend.rentalBusiness.module.payment.provider;

import com.backend.rentalBusiness.module.payment.dto.request.CreatePaymentRequest;

public interface PaymentProvider {

    String initiatePayment(CreatePaymentRequest request);

    boolean verifyPayment(String transactionId);

}