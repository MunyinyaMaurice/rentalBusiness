package com.backend.rentalBusiness.module.paymentAccount.service.impl;

import com.backend.rentalBusiness.module.business.entity.BusinessModel;
import com.backend.rentalBusiness.module.business.repository.BusinessRepository;
import com.backend.rentalBusiness.module.paymentAccount.dto.request.CreatePaymentAccountRequest;
import com.backend.rentalBusiness.module.paymentAccount.dto.request.CreateSubaccountRequest;
import com.backend.rentalBusiness.module.paymentAccount.dto.request.OnboardBusinessRequest;
import com.backend.rentalBusiness.module.paymentAccount.dto.request.ValidateBankRequest;
import com.backend.rentalBusiness.module.paymentAccount.dto.response.OnboardBusinessResponse;
import com.backend.rentalBusiness.module.paymentAccount.dto.response.PaymentAccountResponse;
import com.backend.rentalBusiness.module.paymentAccount.dto.response.ValidateBankResponse;
import com.backend.rentalBusiness.module.paymentAccount.entity.PaymentAccount;
import com.backend.rentalBusiness.module.paymentAccount.mapper.PaymentAccountMapper;
import com.backend.rentalBusiness.module.paymentAccount.provider.flutterwave.FlutterwaveSubaccountService;
import com.backend.rentalBusiness.module.paymentAccount.repository.PaymentAccountRepository;
import com.backend.rentalBusiness.module.paymentAccount.service.PaymentAccountService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import java.util.*;


@Service
@RequiredArgsConstructor
public class PaymentAccountServiceImpl implements PaymentAccountService {

    private final PaymentAccountRepository repository;
    private final BusinessRepository businessRepository;
    private final PaymentAccountMapper mapper;
    private final FlutterwaveSubaccountService flutterwaveSubaccountService;
    private final RestTemplate restTemplate;

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

    @Override
public PaymentAccountResponse createSubaccount(CreateSubaccountRequest request) {

    BusinessModel business =
            businessRepository.findById(request.businessId())
                    .orElseThrow(() -> new RuntimeException("Business not found"));

    String subaccountId =
            flutterwaveSubaccountService.createSubaccount(request);

    PaymentAccount account = PaymentAccount.builder()
            .business(business)
            .provider("FLUTTERWAVE")
            .subaccountId(subaccountId)
            .bankName(request.bankCode())
            .accountNumber(request.accountNumber())
            .splitRatio(request.splitRatio())
            .payoutsEnabled(true)
            .detailsSubmitted(true)
            .build();

    repository.save(account);

    return mapper.toResponse(account);
}


    @Value("${flutterwave.secretKey}")
    private String secretKey;

    @Override
    public ValidateBankResponse validateBankAccount(ValidateBankRequest request) {

        String url = "https://api.flutterwave.com/v3/accounts/resolve";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(secretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("account_number", request.accountNumber());
        body.put("account_bank", request.bankCode());

        HttpEntity<Map<String, Object>> entity =
                new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.POST,
                            entity,
                            Map.class
                    );

            Map data = (Map) response.getBody().get("data");

            String accountName = data.get("account_name").toString();

            return new ValidateBankResponse(accountName, true);

        } catch (Exception e) {
            return new ValidateBankResponse(null, false);
        }
    }

    @Override
@Transactional
public OnboardBusinessResponse onboardBusiness(OnboardBusinessRequest request) {

    // 1️⃣ Validate Business
    BusinessModel business =
            businessRepository.findById(request.businessId())
                    .orElseThrow(() -> new RuntimeException("Business not found"));

    // 2️⃣ Check if already onboarded
    Optional<PaymentAccount> existing =
            repository.findByBusinessId(request.businessId());

//     if (existing.isPresent()) {
//         throw new RuntimeException("Business already onboarded");
//     }

        if (existing.isPresent()) {

    PaymentAccount acc = existing.get();

    return new OnboardBusinessResponse(
            "Business already onboarded",
            null,
            acc.getSubaccountId(),
            true,
            true
    );
}

    // 3️⃣ Validate bank account
    ValidateBankResponse validation =
            validateBankAccount(
                    new ValidateBankRequest(
                            request.accountNumber(),
                            request.bankCode()
                    )
            );

    if (!validation.valid()) {
        throw new RuntimeException("Invalid bank account");
    }

    // 4️⃣ Create Flutterwave subaccount
    CreateSubaccountRequest subReq =
            new CreateSubaccountRequest(
                    request.businessId(),
                    request.bankCode(),
                    request.accountNumber(),
                    business.getBusinessName(),
                    business.getEmail(),
                    request.splitRatio()
            );

    String subaccountId =
            flutterwaveSubaccountService.createSubaccount(subReq);

    // 5️⃣ Save PaymentAccount
    PaymentAccount account = PaymentAccount.builder()
            .business(business)
            .provider("FLUTTERWAVE")
            .subaccountId(subaccountId)
            .bankName(request.bankCode())
            .accountNumber(request.accountNumber())
            .splitRatio(request.splitRatio())
            .payoutsEnabled(true)
            .detailsSubmitted(true)
            .build();

    repository.save(account);

    // 6️⃣ Return response
    return new OnboardBusinessResponse(
            "Business successfully onboarded",
            validation.accountName(),
            subaccountId,
            false,
            true
    );
}
}