package com.backend.rentalBusiness.module.rentalTransaction.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.backend.rentalBusiness.module.rentalTransaction.dto.response.RentalLineResponse;
import com.backend.rentalBusiness.module.rentalTransaction.entity.RentalLine;


@Mapper(componentModel = "spring")
public interface RentalLineMapper {

    @Mapping(target = "assetId", source = "asset.id")
    @Mapping(target = "price", source = "priceAtRent")
    @Mapping(target = "returnedQuantity", source = "returnedQuantity")
    @Mapping(
        target = "remaining",
        expression = "java(line.getQuantity() - line.getReturnedQuantity())"
    )
    RentalLineResponse toResponse(RentalLine line);

}
// @Mapper(componentModel = "spring")
// public interface RentalLineMapper {

//     @Mapping(target = "assetId", source = "asset.id")
//     @Mapping(target = "price", source = "priceAtRent")
//     RentalLineResponse toResponse(RentalLine line);

// }