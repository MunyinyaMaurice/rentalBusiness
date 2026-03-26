package com.backend.rentalBusiness.module.invoice.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.*;

import com.backend.rentalBusiness.module.invoice.entity.Invoice;
import com.backend.rentalBusiness.module.invoice.service.InvoiceService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService service;

    @GetMapping("/rental/{rentalId}")
    public Invoice getByRental(@PathVariable UUID rentalId) {
        return service.getByRental(rentalId);
    }
}