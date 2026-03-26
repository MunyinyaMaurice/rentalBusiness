package com.backend.rentalBusiness.module.invoice.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.rentalBusiness.module.invoice.entity.Invoice;

public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {

    Optional<Invoice> findByRental_Id(UUID rentalId);
}