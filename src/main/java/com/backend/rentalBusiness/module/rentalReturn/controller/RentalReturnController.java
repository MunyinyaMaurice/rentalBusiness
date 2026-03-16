package com.backend.rentalBusiness.module.rentalReturn.controller;

import java.util.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.rentalBusiness.module.rentalReturn.dto.request.ReturnAssetRequest;
import com.backend.rentalBusiness.module.rentalReturn.dto.response.ReturnAssetResponse;
import com.backend.rentalBusiness.module.rentalReturn.service.RentalReturnService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/returns")
@RequiredArgsConstructor
public class RentalReturnController {

    private final RentalReturnService service;

    @PostMapping
    public ReturnAssetResponse returnAsset(
            @RequestBody ReturnAssetRequest request
    ) {
        return service.returnAsset(request);
    }

    @GetMapping("/rental/{rentalId}")
    public List<ReturnAssetResponse> getByRental(
            @PathVariable UUID rentalId
    ) {
        return service.getReturnsByRental(rentalId);
    }
//     @GetMapping("/rental/{rentalId}")
// public List<ReturnAssetResponse> getReturnsByRental(
//         @PathVariable UUID rentalId
// ) {
//     return service.getReturnsByRental(rentalId);
// }
}