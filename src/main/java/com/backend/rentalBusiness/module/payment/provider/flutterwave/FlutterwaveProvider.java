package com.backend.rentalBusiness.module.payment.provider.flutterwave;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.backend.rentalBusiness.module.payment.dto.request.CreatePaymentRequest;
import com.backend.rentalBusiness.module.payment.provider.PaymentProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FlutterwaveProvider implements PaymentProvider {

    private final RestTemplate restTemplate;

    @Value("${flutterwave.secretKey}")
    private String secretKey;

    @Override
    public String initiatePayment(CreatePaymentRequest request) {

        String url = "https://api.flutterwave.com/v3/payments";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(secretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Map<String, Object> body = new HashMap<>();

        // body.put("tx_ref", "rental_" + UUID.randomUUID());
        // body.put("amount", request.amount());
        // body.put("currency", "USD");

        // Map<String, Object> customer = new HashMap<>();
        // customer.put("email", "test@test.com");

        // body.put("customer", customer);

        // Map<String, Object> customization = new HashMap<>();
        // customization.put("title", "Rental Payment");

        // body.put("customizations", customization);
        Map<String, Object> body = new HashMap<>();

body.put("tx_ref", "rental_" + UUID.randomUUID());
body.put("amount", request.amount());
body.put("currency", "USD");
body.put("redirect_url", "http://localhost:23990/api/payments/verify");

Map<String, Object> customer = new HashMap<>();
customer.put("email", "test@test.com");
customer.put("name", "Test User");

body.put("customer", customer);

Map<String, Object> customization = new HashMap<>();
customization.put("title", "Rental Payment");
customization.put("description", "Payment for rental");

body.put("customizations", customization);

        HttpEntity<Map<String, Object>> entity =
                new HttpEntity<>(body, headers);

        ResponseEntity<Map> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.POST,
                        entity,
                        Map.class
                );

        Map data = (Map) response.getBody().get("data");

        return data.get("link").toString();
    }

    @Override
    public boolean verifyPayment(String transactionId) {
        return true;
    }
}