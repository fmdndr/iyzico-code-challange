package com.iyzico.challenge.validator;


import com.iyzico.challenge.entity.Flight;
import com.iyzico.challenge.exception.BadRequestException;
import com.iyzico.challenge.exception.ResourceNotFoundException;
import com.iyzico.challenge.repository.FlightRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.*;

@Component
@AllArgsConstructor
public class FlightValidator {
    private final FlightRepository flightRepository;
    private final Logger logger = LoggerFactory.getLogger(FlightValidator.class);

    public void isValidToCreate(Flight flight) {
        logger.info("flight : " + flight.getId());
        boolean isExists = flightRepository.existsByNameAndCreationDateLessThanEqual(flight.getName(), getEndOfTheDayInMillis());

        if (isExists) {
            throw new BadRequestException("flight already exists : " + flight.getName());
        }

        if (flight.getName().equals("")) {
            throw new BadRequestException("Flight name can not be null");
        }
    }

    public void isValidToEdit(Flight flight) {
        logger.info("flight: " + flight.getId());

        boolean isExists = flightRepository.existsById(flight.getId());

        if (!isExists) {
            throw new ResourceNotFoundException("Flight not found with this id: " + flight.getId());
        }

        if (flight.getName().trim().equals("")) {
            throw new BadRequestException("Flight name can not be null");
        }
    }

    public void isExistsByFlightId(Long flightId) {
        boolean isExistsById = flightRepository.existsById(flightId);
        if (!isExistsById) {
            throw new ResourceNotFoundException("Flight could not be found with this id : " + flightId);
        }
    }

    private Long getEndOfTheDayInMillis() {

        OffsetDateTime odt = OffsetDateTime.now();
        ZoneOffset zoneOffset = odt.getOffset();
        long endOfTheDay = LocalDateTime.now()
                .with(LocalTime.MAX)
                .toInstant(zoneOffset)
                .toEpochMilli();

        logger.info("endOfTheDay : " + endOfTheDay);
        return endOfTheDay;
    }
}
