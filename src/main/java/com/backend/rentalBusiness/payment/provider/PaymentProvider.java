package com.backend.rentalBusiness.payment.provider;

import com.backend.rentalBusiness.payment.dto.request.CreatePaymentRequest;

public interface PaymentProvider {

    String initiatePayment(CreatePaymentRequest request);

    boolean verifyPayment(String transactionId);

}