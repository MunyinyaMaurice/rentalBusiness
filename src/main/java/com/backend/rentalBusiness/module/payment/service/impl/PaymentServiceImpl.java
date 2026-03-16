package com.backend.rentalBusiness.module.payment.service.impl;

import java.util.*;

import org.hibernate.sql.Template;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.backend.rentalBusiness.module.payment.dto.request.CreatePaymentRequest;
import com.backend.rentalBusiness.module.payment.dto.response.PaymentResponse;
import com.backend.rentalBusiness.module.payment.entity.Payment;
import com.backend.rentalBusiness.module.payment.mapper.PaymentMapper;
import com.backend.rentalBusiness.module.payment.provider.PaymentProvider;
import com.backend.rentalBusiness.module.payment.repository.PaymentRepository;
import com.backend.rentalBusiness.module.payment.service.PaymentService;
import com.backend.rentalBusiness.module.rentalTransaction.entity.RentalTransaction;
import com.backend.rentalBusiness.module.rentalTransaction.repository.RentalTransactionRepository;
import org.springframework.beans.factory.annotation.Value;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository repository;
    private final RentalTransactionRepository rentalRepository;
    private final PaymentMapper mapper;
    private final PaymentProvider paymentProvider;
    private final RestTemplate restTemplate;

    @Value("${flutterwave.secretKey}")
    private String secretKey;

    @Override
    public PaymentResponse createPayment(CreatePaymentRequest request) {

        RentalTransaction rental =
                rentalRepository.findById(request.rentalId())
                        .orElseThrow(() ->
                                new RuntimeException("Rental not found"));

        String paymentUrl =
                paymentProvider.initiatePayment(request);

        Payment payment = Payment.builder()
                .rentalTransaction(rental)
                .amount(request.amount())
                .method(request.method())
                .provider("FLUTTERWAVE")
                .status("PENDING")
                .build();

        repository.save(payment);

        PaymentResponse response = mapper.toResponse(payment);

        return new PaymentResponse(
                response.id(),
                response.rentalId(),
                response.amount(),
                response.method(),
                response.status(),
                paymentUrl
        );
    }

    @Override
    public List<PaymentResponse> getPaymentsByRental(UUID rentalId) {

        return repository.findByRentalTransactionId(rentalId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public boolean verifyFlutterwavePayment(String transactionId, String txRef) {

        String url = "https://api.flutterwave.com/v3/transactions/" + transactionId + "/verify";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(secretKey);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        entity,
                        Map.class
                );

        Map data = (Map) response.getBody().get("data");

        String status = data.get("status").toString();

        if (!"successful".equalsIgnoreCase(status)) {
            return false;
        }

        Payment payment =
                repository.findByTxRef(txRef).orElseThrow();

        payment.setStatus("SUCCESS");

        repository.save(payment);

        return true;
    }
    @Override
public void handleFlutterwaveWebhook(Map<String, Object> payload) {

    String event = payload.get("event").toString();

    if (!"charge.completed".equals(event)) {
        return;
    }

    Map data = (Map) payload.get("data");

    String status = data.get("status").toString();
    String txRef = data.get("tx_ref").toString();

    if (!"successful".equalsIgnoreCase(status)) {
        return;
    }

    Payment payment =
            repository.findByTxRef(txRef)
                    .orElseThrow();

    payment.setStatus("SUCCESS");

    repository.save(payment);
}
}