package com.backend.rentalBusiness.module.paymentAccount.provider.flutterwave;

import java.util.*;
import org.springframework.http.*;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.backend.rentalBusiness.module.paymentAccount.dto.request.CreateSubaccountRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FlutterwaveSubaccountService {

    private final RestTemplate restTemplate;

    @Value("${flutterwave.secretKey}")
    private String secretKey;

    public String createSubaccount(CreateSubaccountRequest request) {

        String url = "https://api.flutterwave.com/v3/subaccounts";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(secretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();

        body.put("account_bank", request.bankCode());
        body.put("account_number", request.accountNumber());
        body.put("business_name", request.businessName());
        body.put("business_email", request.businessEmail());
        body.put("split_value", request.splitRatio());
        body.put("split_type", "percentage");

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

        return data.get("subaccount_id").toString();
    }
}


// package com.backend.rentalBusiness.module.paymentAccount.provider.flutterwave;

// import java.util.*;
// import org.springframework.http.*;
// import org.springframework.http.MediaType;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Service;
// import org.springframework.web.client.RestTemplate;

// import com.backend.rentalBusiness.module.paymentAccount.dto.request.CreateSubaccountRequest;

// import lombok.RequiredArgsConstructor;

// @Service
// @RequiredArgsConstructor
// public class FlutterwaveSubaccountService {

//     private final RestTemplate restTemplate;

//     @Value("${flutterwave.secretKey}")
//     private String secretKey;

//     public String createSubaccount(CreateSubaccountRequest request) {

//         String url = "https://api.flutterwave.com/v3/subaccounts";

//         HttpHeaders headers = new HttpHeaders();
//         headers.setBearerAuth(secretKey);
//         headers.setContentType(MediaType.APPLICATION_JSON);

//         Map<String, Object> body = new HashMap<>();

//         body.put("account_bank", request.bankCode());
//         body.put("account_number", request.accountNumber());
//         body.put("business_name", request.businessName());
//         body.put("business_email", request.businessEmail());
//         body.put("split_value", request.splitRatio());
//         body.put("split_type", "percentage");

//         HttpEntity<Map<String, Object>> entity =
//                 new HttpEntity<>(body, headers);

//         ResponseEntity<Map> response =
//                 restTemplate.exchange(
//                         url,
//                         HttpMethod.POST,
//                         entity,
//                         Map.class
//                 );

//         Map data = (Map) response.getBody().get("data");

//         return data.get("subaccount_id").toString();
//     }
// }