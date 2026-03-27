package com.backend.rentalBusiness.module.lateFee.service;

import java.math.BigDecimal;

import com.backend.rentalBusiness.module.rentalTransaction.entity.RentalTransaction;

public interface LateFeeService {

    BigDecimal calculateLateFee(RentalTransaction rental);

}
