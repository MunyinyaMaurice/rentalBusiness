package com.backend.rentalBusiness.lateFee.service;

import java.math.BigDecimal;

import com.backend.rentalBusiness.rentalTransaction.entity.RentalTransaction;

public interface LateFeeService {

    BigDecimal calculateLateFee(RentalTransaction rental);

}
