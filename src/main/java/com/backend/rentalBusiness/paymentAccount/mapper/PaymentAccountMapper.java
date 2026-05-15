package com.backend.rentalBusiness.paymentAccount.mapper;

import com.backend.rentalBusiness.paymentAccount.dto.response.PaymentAccountResponse;
import com.backend.rentalBusiness.paymentAccount.entity.PaymentAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentAccountMapper {

    @Mapping(target = "businessId", source = "business.id")
    PaymentAccountResponse toResponse(PaymentAccount account);

}