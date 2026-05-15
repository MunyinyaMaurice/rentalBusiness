package com.backend.rentalBusiness.payment.mapper;

import com.backend.rentalBusiness.payment.dto.response.PaymentResponse;
import com.backend.rentalBusiness.payment.entity.Payment;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(target = "rentalId", source = "rentalTransaction.id")
    @Mapping(target = "paymentUrl", ignore = true)
    PaymentResponse toResponse(Payment payment);

}