package com.backend.rentalBusiness.module.invoice.service;

import java.util.UUID;

import com.backend.rentalBusiness.module.invoice.entity.Invoice;
import com.backend.rentalBusiness.module.rentalTransaction.entity.RentalTransaction;

public interface InvoiceService {

    Invoice generateInvoice(RentalTransaction rental);

    Invoice getByRental(UUID rentalId);
}
