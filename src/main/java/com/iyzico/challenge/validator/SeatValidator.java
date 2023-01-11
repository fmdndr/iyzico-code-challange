package com.iyzico.challenge.validator;

import com.iyzico.challenge.constants.SeatStatus;
import com.iyzico.challenge.entity.Seat;
import com.iyzico.challenge.exception.ResourceHasBeenSoldException;
import com.iyzico.challenge.exception.ResourceNotFoundException;
import com.iyzico.challenge.repository.SeatRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SeatValidator {

    private final Logger logger = LoggerFactory.getLogger(SeatValidator.class);

    private final SeatRepository seatRepository;

    public void isValidToEdit(Seat seat) {
        boolean isExists = seatRepository.existsById(seat.getId());
        if (!isExists) {
            throw new ResourceNotFoundException("Seat not found with this id : " + seat.getId());
        }
    }

    public void isValidToBuy(Seat seat) {
        Seat isSeatValid = seatRepository.findById(seat.getId()).orElse(null);

        if (isSeatValid != null && !isSeatValid.getSeatStatus().equals(SeatStatus.AVAILABLE)) {
            throw new ResourceHasBeenSoldException("Seat already sold to another customer or not available for sell : " + seat.getId());
        }

    }


}
