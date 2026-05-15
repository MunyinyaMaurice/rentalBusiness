package com.backend.rentalBusiness.payment.provider.flutterwave;

import java.util.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.backend.rentalBusiness.payment.dto.request.CreatePaymentRequest;
import com.backend.rentalBusiness.payment.provider.PaymentProvider;
import com.backend.rentalBusiness.paymentAccount.entity.PaymentAccount;
import com.backend.rentalBusiness.paymentAccount.repository.PaymentAccountRepository;
import com.backend.rentalBusiness.rentalTransaction.entity.RentalTransaction;
import com.backend.rentalBusiness.rentalTransaction.repository.RentalTransactionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FlutterwaveProvider implements PaymentProvider {

    private final RestTemplate restTemplate;
    private final PaymentAccountRepository paymentAccountRepository;
    private final RentalTransactionRepository rentalRepository;

    @Value("${flutterwave.secretKey}")
    private String secretKey;

    @Override
    public String initiatePayment(CreatePaymentRequest request) {

        String url = "https://api.flutterwave.com/v3/payments";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(secretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 🔥 1. Get business payment account
        // PaymentAccount account =
        //         paymentAccountRepository.findByBusinessId(request.getBusinessId())
        //                 .orElseThrow(() ->
        //                         new RuntimeException("Business not onboarded for payments"));
        RentalTransaction rental =
        rentalRepository.findById(request.rentalId())
                .orElseThrow(() -> new RuntimeException("Rental not found"));

UUID businessId = rental.getBusiness().getId();

PaymentAccount account =
        paymentAccountRepository.findByBusinessId(businessId)
                .orElseThrow(() ->
                        new RuntimeException("Business not onboarded for payments"));

        // 🔥 2. Generate tx_ref (IMPORTANT)
        String txRef = "rental_" + UUID.randomUUID();

        Map<String, Object> body = new HashMap<>();

        body.put("tx_ref", txRef);
        body.put("amount", request.amount());
        body.put("currency", "USD");
        body.put("redirect_url", "http://localhost:23990/api/payments/verify");

        // 🔥 Customer info
        Map<String, Object> customer = new HashMap<>();
        customer.put("email", "test@test.com");
        customer.put("name", "Test User");

        body.put("customer", customer);

        // 🔥 UI customization
        Map<String, Object> customization = new HashMap<>();
        customization.put("title", "Rental Payment");
        customization.put("description", "Payment for rental");

        body.put("customizations", customization);

        // 🔥 3. Split payment (SUBACCOUNT)
        Map<String, Object> sub = new HashMap<>();
        sub.put("id", account.getSubaccountId());

        // Optional: commission split
        sub.put("transaction_split_ratio", account.getSplitRatio() * 100);

        List<Map<String, Object>> subs = List.of(sub);

        body.put("subaccounts", subs);

        HttpEntity<Map<String, Object>> entity =
                new HttpEntity<>(body, headers);

        try {

            log.info("Initiating Flutterwave payment for business: {}", businessId);

            ResponseEntity<Map> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.POST,
                            entity,
                            Map.class
                    );

            Map responseBody = response.getBody();

            if (responseBody == null || !"success".equals(responseBody.get("status"))) {
                throw new RuntimeException("Flutterwave payment creation failed");
            }

            Map data = (Map) responseBody.get("data");

            if (data == null || data.get("link") == null) {
                throw new RuntimeException("Invalid Flutterwave response");
            }

            String paymentLink = data.get("link").toString();

            log.info("Payment link generated: {}", paymentLink);

            return paymentLink;

        } catch (Exception e) {

            log.error("Flutterwave payment initiation failed", e);

            throw new RuntimeException("Payment failed: " + e.getMessage());
        }
    }

    @Override
    public boolean verifyPayment(String transactionId) {
        return true;
    }
}