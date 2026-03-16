package com.backend.rentalBusiness.module.payment.controller;

import com.backend.rentalBusiness.module.payment.dto.request.CreatePaymentRequest;
import com.backend.rentalBusiness.module.payment.dto.response.PaymentResponse;
import com.backend.rentalBusiness.module.payment.provider.PaymentProvider;
import com.backend.rentalBusiness.module.payment.service.PaymentService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService service;
    private final PaymentProvider paymentProvider;

    @PostMapping
    public PaymentResponse createPayment(
            @RequestBody CreatePaymentRequest request) {

        return service.createPayment(request);
    }

    @GetMapping("/rental/{rentalId}")
    public List<PaymentResponse> getPaymentsByRental(
            @PathVariable UUID rentalId) {

        return service.getPaymentsByRental(rentalId);
    }
    @GetMapping("/verify")
public String verifyPayment(
        @RequestParam("transaction_id") String transactionId,
        @RequestParam("status") String status) {

    if (!"successful".equals(status)) {
        return "Payment failed";
    }

    boolean verified = paymentProvider.verifyPayment(transactionId);

    if (verified) {
        return "Payment verified successfully";
    }

    return "Verification failed";
}

// PRODUCTION READY ENDPOINT
// @GetMapping("/verify")
// public ResponseEntity<String> verifyPayment(
//         @RequestParam String transaction_id,
//         @RequestParam String status,
//         @RequestParam String tx_ref
// ) {

//     if (!"successful".equalsIgnoreCase(status)) {
//         return ResponseEntity.badRequest().body("Payment failed");
//     }

//     boolean verified = service.verifyFlutterwavePayment(transaction_id, tx_ref);

//     if (!verified) {
//         return ResponseEntity.badRequest().body("Verification failed");
//     }

//     return ResponseEntity.ok("Payment verified");
// }
}