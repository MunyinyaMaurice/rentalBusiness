package com.backend.rentalBusiness.module.store.mapper;

import com.backend.rentalBusiness.module.store.dto.request.CreateStoreRequest;
import com.backend.rentalBusiness.module.store.dto.response.StoreResponse;
import com.backend.rentalBusiness.module.store.entity.Store;

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