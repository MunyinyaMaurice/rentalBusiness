package com.backend.rentalBusiness.module.payment.service;

import java.util.*;

import com.backend.rentalBusiness.module.payment.dto.request.CreatePaymentRequest;
import com.backend.rentalBusiness.module.payment.dto.response.PaymentResponse;

public interface PaymentService {

    PaymentResponse createPayment(CreatePaymentRequest request);

    List<PaymentResponse> getPaymentsByRental(UUID rentalId);
    boolean verifyFlutterwavePayment(String transactionId, String txRef);
    void handleFlutterwaveWebhook(Map<String, Object> payload);

}