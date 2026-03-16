package com.backend.rentalBusiness.module.business.mapper;

import com.backend.rentalBusiness.module.business.dto.request.CreateBusinessRequest;
import com.backend.rentalBusiness.module.business.dto.response.BusinessResponse;
import com.backend.rentalBusiness.module.business.entity.BusinessModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BusinessMapper {

    BusinessModel toEntity(CreateBusinessRequest request);

    BusinessResponse toResponse(BusinessModel entity);
}