package com.backend.rentalBusiness.rentalReturn.service;

import java.util.*;

import com.backend.rentalBusiness.rentalReturn.dto.request.ReturnAssetRequest;
import com.backend.rentalBusiness.rentalReturn.dto.response.ReturnAssetResponse;

public interface RentalReturnService {

    ReturnAssetResponse returnAsset(ReturnAssetRequest request);

    List<ReturnAssetResponse> getReturnsByRental(UUID rentalId);

}
