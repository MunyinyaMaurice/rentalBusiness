package com.backend.rentalBusiness.module.lateFee.service.impl;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Service;

import com.backend.rentalBusiness.module.lateFee.entity.LateFee;
import com.backend.rentalBusiness.module.lateFee.repository.LateFeeRepository;
import com.backend.rentalBusiness.module.lateFee.service.LateFeeService;
import com.backend.rentalBusiness.module.rentalTransaction.entity.RentalTransaction;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class LateFeeServiceImpl implements LateFeeService {

    private final LateFeeRepository lateFeeRepository;

    @Override
    public BigDecimal calculateLateFee(RentalTransaction rental) {

        Instant now = Instant.now();

        if (!now.isAfter(rental.getDueDate())) {
            return BigDecimal.ZERO;
        }

        int daysLate =
                (int) ChronoUnit.DAYS.between(
                        rental.getDueDate(),
                        now
                );

        BigDecimal feePerDay =
                rental.getBusiness().getLateFeePerDay();

        BigDecimal lateFee =
                feePerDay.multiply(BigDecimal.valueOf(daysLate));

        // Save LateFee record
        LateFee fee = LateFee.builder()
                .rentalTransaction(rental)
                .daysLate(daysLate)
                .feePerDay(feePerDay)
                .totalFee(lateFee)
                .waived(false)
                .paid(false)
                .build();

        lateFeeRepository.save(fee);

        return lateFee;
    }
}