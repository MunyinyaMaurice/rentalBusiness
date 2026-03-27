package com.backend.rentalBusiness.module.payment.controller;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.backend.rentalBusiness.module.payment.service.PaymentService;

import lombok.RequiredArgsConstructor;
@RestController
@RequestMapping("/api/payments/flutterwave")
@RequiredArgsConstructor
public class FlutterwaveWebhookController {

    private final PaymentService paymentService;

    @Value("${flutterwave.secretHash}")
    private String secretHash;

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(
            @RequestHeader(value = "verif-hash", required = false) String hash,
            @RequestBody Map<String, Object> payload
    ) {

        // 🔐 1. Validate webhook
        if (hash == null || !hash.equals(secretHash)) {
            return ResponseEntity.status(401).body("Invalid webhook");
        }

        paymentService.handleFlutterwaveWebhook(payload);

        return ResponseEntity.ok("Webhook processed");
    }
}


// @RestController
// @RequestMapping("/api/payments/flutterwave")
// @RequiredArgsConstructor
// public class FlutterwaveWebhookController {

//     private final PaymentService paymentService;

//     @Value("${flutterwave.secretHash}")
//     private String secretHash;

//     @PostMapping("/webhook")
//     public ResponseEntity<?> handleWebhook(
//             @RequestHeader(value = "verif-hash", required = false) String hash,
//             @RequestBody Map<String, Object> payload
//     ) {

//         if (hash == null || !hash.equals(secretHash)) {
//             return ResponseEntity.status(401).body("Invalid webhook");
//         }

//         paymentService.handleFlutterwaveWebhook(payload);

//         return ResponseEntity.ok("Webhook received");
//     }
// }