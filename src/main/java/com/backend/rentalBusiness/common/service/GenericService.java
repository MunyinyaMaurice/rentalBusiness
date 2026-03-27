package com.backend.rentalBusiness.common.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface GenericService<T, RQ, RS> {

    RS create(RQ request);

    RS get(UUID id);

    Page<RS> getAll(Pageable pageable);

    RS update(UUID id, RQ request);

    void delete(UUID id);
}