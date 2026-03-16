package com.backend.rentalBusiness.module.rentalReturn.mapper;

import org.mapstruct.*;

import com.backend.rentalBusiness.module.rentalReturn.dto.response.ReturnAssetResponse;
import com.backend.rentalBusiness.module.rentalReturn.entity.RentalReturn;

@Mapper(componentModel = "spring")
public interface RentalReturnMapper {

    @Mapping(target = "rentalTransactionId", source = "rentalTransaction.id")
    @Mapping(target = "assetId", source = "asset.id")
    ReturnAssetResponse toResponse(RentalReturn entity);

}


