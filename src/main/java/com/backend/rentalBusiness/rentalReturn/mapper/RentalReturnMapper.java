package com.backend.rentalBusiness.rentalReturn.mapper;

import org.mapstruct.*;

import com.backend.rentalBusiness.rentalReturn.dto.response.ReturnAssetResponse;
import com.backend.rentalBusiness.rentalReturn.entity.RentalReturn;

@Mapper(componentModel = "spring")
public interface RentalReturnMapper {

    @Mapping(target = "rentalTransactionId", source = "rentalTransaction.id")
    @Mapping(target = "assetId", source = "asset.id")
    ReturnAssetResponse toResponse(RentalReturn entity);

}


