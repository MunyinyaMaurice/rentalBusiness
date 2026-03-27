package com.backend.rentalBusiness.module.paymentAccount.mapper;

import com.backend.rentalBusiness.module.paymentAccount.dto.response.PaymentAccountResponse;
import com.backend.rentalBusiness.module.paymentAccount.entity.PaymentAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentAccountMapper {

    @Mapping(target = "businessId", source = "business.id")
    PaymentAccountResponse toResponse(PaymentAccount account);

}