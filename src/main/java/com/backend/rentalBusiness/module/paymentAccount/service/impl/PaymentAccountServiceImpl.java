package com.backend.rentalBusiness.module.paymentAccount.service.impl;

import com.backend.rentalBusiness.module.business.entity.BusinessModel;
import com.backend.rentalBusiness.module.business.repository.BusinessRepository;
import com.backend.rentalBusiness.module.paymentAccount.dto.request.CreatePaymentAccountRequest;
import com.backend.rentalBusiness.module.paymentAccount.dto.response.PaymentAccountResponse;
import com.backend.rentalBusiness.module.paymentAccount.entity.PaymentAccount;
import com.backend.rentalBusiness.module.paymentAccount.mapper.PaymentAccountMapper;
import com.backend.rentalBusiness.module.paymentAccount.repository.PaymentAccountRepository;
import com.backend.rentalBusiness.module.paymentAccount.service.PaymentAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentAccountServiceImpl implements PaymentAccountService {

    private final PaymentAccountRepository repository;
    private final BusinessRepository businessRepository;
    private final PaymentAccountMapper mapper;

    @Override
    public PaymentAccountResponse createAccount(CreatePaymentAccountRequest request) {

        BusinessModel business =
                businessRepository.findById(request.businessId())
                        .orElseThrow(() -> new RuntimeException("Business not found"));

        // Normally this comes from Stripe / Flutterwave
        String providerAccountId = "acct_" + UUID.randomUUID();

        PaymentAccount account = PaymentAccount.builder()
                .business(business)
                .provider(request.provider())
                .providerAccountId(providerAccountId)
                .country(request.country())
                .currency(request.currency())
                .payoutsEnabled(false)
                .detailsSubmitted(false)
                .build();

        repository.save(account);

        return mapper.toResponse(account);
    }

    @Override
    public PaymentAccountResponse getByBusiness(UUID businessId) {

        PaymentAccount account =
                repository.findByBusinessId(businessId)
                        .orElseThrow(() -> new RuntimeException("Payment account not found"));

        return mapper.toResponse(account);
    }
}