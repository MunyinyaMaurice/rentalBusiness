package com.backend.rentalBusiness.module.rentalTransaction.mapper;

import com.backend.rentalBusiness.module.rentalTransaction.dto.response.RentalTransactionResponse;
import com.backend.rentalBusiness.module.rentalTransaction.entity.RentalTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = RentalLineMapper.class)
public interface RentalTransactionMapper {

    @Mapping(target = "businessId", source = "business.id")
    @Mapping(target = "storeId", source = "store.id")
    RentalTransactionResponse toResponse(RentalTransaction entity);

}