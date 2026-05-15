package com.backend.rentalBusiness.store.mapper;

import com.backend.rentalBusiness.store.dto.request.CreateStoreRequest;
import com.backend.rentalBusiness.store.dto.response.StoreResponse;
import com.backend.rentalBusiness.store.entity.Store;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

// @Mapper(
//     componentModel = "spring"
// )
// public interface StoreMapper {

//     @Mapping(target = "business", ignore = true)
//     Store toEntity(CreateStoreRequest request);

//     @Mapping(target = "businessId", source = "business.id")
//     StoreResponse toResponse(Store store);
// }
@Mapper(componentModel = "spring")
public interface StoreMapper {

    @Mapping(target = "business", ignore = true)
    Store toEntity(CreateStoreRequest request);

    @Mapping(target = "businessId", source = "business.id")
    StoreResponse toResponse(Store store);
}