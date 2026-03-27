package com.backend.rentalBusiness.module.rentalReturn.service;

import java.util.*;

import com.backend.rentalBusiness.module.rentalReturn.dto.request.ReturnAssetRequest;
import com.backend.rentalBusiness.module.rentalReturn.dto.response.ReturnAssetResponse;

public interface RentalReturnService {

    ReturnAssetResponse returnAsset(ReturnAssetRequest request);

    List<ReturnAssetResponse> getReturnsByRental(UUID rentalId);

}
