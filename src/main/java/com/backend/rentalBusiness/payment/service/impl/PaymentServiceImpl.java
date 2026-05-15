package com.backend.rentalBusiness.payment.service.impl;

import java.util.*;


import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.backend.rentalBusiness.invoice.service.InvoiceService;
import com.backend.rentalBusiness.payment.dto.request.CreatePaymentRequest;
import com.backend.rentalBusiness.payment.dto.response.PaymentResponse;
import com.backend.rentalBusiness.payment.entity.Payment;
import com.backend.rentalBusiness.payment.mapper.PaymentMapper;
import com.backend.rentalBusiness.payment.provider.PaymentProvider;
import com.backend.rentalBusiness.payment.repository.PaymentRepository;
import com.backend.rentalBusiness.payment.service.PaymentService;
import com.backend.rentalBusiness.rentalTransaction.entity.RentalStatus;
import com.backend.rentalBusiness.rentalTransaction.entity.RentalTransaction;
import com.backend.rentalBusiness.rentalTransaction.repository.RentalTransactionRepository;

import jakarta.transaction.Transactional;

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
    private final InvoiceService invoiceService;

    @Value("${flutterwave.secretKey}")
    private String secretKey;

    @Override
    public PaymentResponse createPayment(CreatePaymentRequest request) {

        // RentalTransaction rental =
        //         rentalRepository.findById(request.rentalId())
        //                 .orElseThrow(() ->
        //                         new RuntimeException("Rental not found"));

         RentalTransaction rental =
        rentalRepository.findById(request.rentalId())
                .orElseThrow(() -> new RuntimeException("Rental not found"));

UUID businessId = rental.getBusiness().getId();

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
@Transactional
public void handleFlutterwaveWebhook(Map<String, Object> payload) {

    String event = payload.get("event").toString();

    // ✅ Only process successful charge
    if (!"charge.completed".equalsIgnoreCase(event)) {
        return;
    }

    Map data = (Map) payload.get("data");

    String status = data.get("status").toString();
    String txRef = data.get("tx_ref").toString();
    String providerTxId = data.get("id").toString();

    if (!"successful".equalsIgnoreCase(status)) {
        return;
    }

    // 🔥 1. Find payment
    Payment payment = repository.findByTxRef(txRef)
            .orElseThrow(() -> new RuntimeException("Payment not found"));

    // 🔒 2. IDEMPOTENCY CHECK (VERY IMPORTANT)
    if ("SUCCESS".equals(payment.getStatus())) {
        return; // already processed → ignore duplicate webhook
    }

    // 🔥 3. Update payment
    payment.setStatus("SUCCESS");
    payment.setProviderTransactionId(providerTxId);

    repository.save(payment);

    // 🔥 4. Update rental
    RentalTransaction rental = payment.getRentalTransaction();

    rental.setStatus(RentalStatus.PAID);
    invoiceService.generateInvoice(rental);

    rentalRepository.save(rental);
}
//     @Override
// public void handleFlutterwaveWebhook(Map<String, Object> payload) {

//     String event = payload.get("event").toString();

//     if (!"charge.completed".equals(event)) {
//         return;
//     }

//     Map data = (Map) payload.get("data");

//     String status = data.get("status").toString();
//     String txRef = data.get("tx_ref").toString();

//     if (!"successful".equalsIgnoreCase(status)) {
//         return;
//     }

//     Payment payment =
//             repository.findByTxRef(txRef)
//                     .orElseThrow();

//     payment.setStatus("SUCCESS");

//     repository.save(payment);
// }
}