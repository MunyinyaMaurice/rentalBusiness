package com.backend.rentalBusiness.invoice.service;

import java.util.UUID;

import com.backend.rentalBusiness.invoice.entity.Invoice;
import com.backend.rentalBusiness.rentalTransaction.entity.RentalTransaction;

public interface InvoiceService {

    Invoice generateInvoice(RentalTransaction rental);

    Invoice getByRental(UUID rentalId);
}
