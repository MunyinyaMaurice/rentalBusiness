package com.backend.rentalBusiness.common.service;

import com.backend.rentalBusiness.common.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public abstract class AbstractCrudService<E, RQ, RS>
        implements GenericService<E, RQ, RS> {

    protected abstract JpaRepository<E, UUID> repository();

    protected abstract E toEntity(RQ request);

    protected abstract RS toResponse(E entity);

    @Override
    public RS create(RQ request) {

        E entity = toEntity(request);
        repository().save(entity);

        return toResponse(entity);
    }

    @Override
    public RS get(UUID id) {

        E entity = repository()
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource"));

        return toResponse(entity);
    }

    @Override
    public Page<RS> getAll(Pageable pageable) {

        return repository()
                .findAll(pageable)
                .map(this::toResponse);
    }

    @Override
    public void delete(UUID id) {

        repository().deleteById(id);
    }
}