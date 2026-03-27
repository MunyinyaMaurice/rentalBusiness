package com.backend.rentalBusiness.module.rentalTransaction.scheduler;

import com.backend.rentalBusiness.module.business.entity.BusinessModel;
import com.backend.rentalBusiness.module.rentalTransaction.entity.RentalStatus;
import com.backend.rentalBusiness.module.rentalTransaction.entity.RentalTransaction;
import com.backend.rentalBusiness.module.rentalTransaction.repository.RentalTransactionRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OverdueRentalScheduler {

    private final RentalTransactionRepository rentalRepository;

    /**
     * Runs every hour
     */
    @Scheduled(cron = "0 0 * * * *")
    public void markOverdueRentals() {

        List<RentalTransaction> activeRentals =
                rentalRepository.findByStatus("ACTIVE");

        Instant now = Instant.now();

        for (RentalTransaction rental : activeRentals) {

            BusinessModel business = rental.getBusiness();

            int graceDays = business.getOverdueGraceDays();

            Instant overdueDate =
                    rental.getDueDate().plus(graceDays, ChronoUnit.DAYS);

            if (now.isAfter(overdueDate)) {

                rental.setStatus(RentalStatus.OVERDUE);

                rentalRepository.save(rental);
            }
        }
    }
}