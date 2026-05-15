package com.backend.rentalBusiness.business.mapper;

import com.backend.rentalBusiness.business.dto.request.CreateBusinessRequest;
import com.backend.rentalBusiness.business.dto.response.BusinessResponse;
import com.backend.rentalBusiness.business.entity.BusinessModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BusinessMapper {

    BusinessModel toEntity(CreateBusinessRequest request);

    BusinessResponse toResponse(BusinessModel entity);
}