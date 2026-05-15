package com.backend.rentalBusiness.invoice.service.impl;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.backend.rentalBusiness.invoice.entity.Invoice;
import com.backend.rentalBusiness.invoice.repository.InvoiceRepository;
import com.backend.rentalBusiness.invoice.service.InvoiceService;
import com.backend.rentalBusiness.rentalReturn.repository.RentalReturnRepository;
import com.backend.rentalBusiness.rentalTransaction.entity.RentalTransaction;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final RentalReturnRepository rentalReturnRepository;

    @Override
    @Transactional
    public Invoice generateInvoice(RentalTransaction rental) {

        // 🔒 prevent duplicate invoice
        Optional<Invoice> existing =
                invoiceRepository.findByRental_Id(rental.getId());

        if (existing.isPresent()) {
            return existing.get();
        }

        BigDecimal subtotal = rental.getSubtotal();
        BigDecimal tax = rental.getTaxAmount();

        // 🔥 Late fee (already calculated in rental or return)
        BigDecimal lateFee = Optional.ofNullable(rental.getTotalAmount())
                .orElse(BigDecimal.ZERO)
                .subtract(subtotal.add(tax));

        if (lateFee.compareTo(BigDecimal.ZERO) < 0) {
            lateFee = BigDecimal.ZERO;
        }

        // 🔥 Damage fees
        BigDecimal damageFee = rentalReturnRepository
                .findByRentalTransactionId(rental.getId())
                .stream()
                .map(r -> r.getDamageCharge())
                .filter(java.util.Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal total = subtotal
                .add(tax)
                .add(lateFee)
                .add(damageFee);

        Invoice invoice = Invoice.builder()
                .rental(rental)
                .subtotal(subtotal)
                .tax(tax)
                .lateFee(lateFee)
                .damageFee(damageFee)
                .total(total)
                .status("PAID")
                .issuedAt(Instant.now())
                .paidAt(Instant.now())
                .build();

        return invoiceRepository.save(invoice);
    }

    @Override
    public Invoice getByRental(UUID rentalId) {
        return invoiceRepository.findByRental_Id(rentalId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));
    }
}