package com.iyzico.challenge.service.implementation;

import com.iyzico.challenge.constants.SeatStatus;
import com.iyzico.challenge.entity.Flight;
import com.iyzico.challenge.entity.Seat;
import com.iyzico.challenge.repository.FlightRepository;
import com.iyzico.challenge.repository.SeatRepository;
import com.iyzico.challenge.service.FlightService;
import com.iyzico.challenge.validator.FlightValidator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightServiceImp implements FlightService {

    private Logger logger = LoggerFactory.getLogger(FlightService.class);
    private final FlightRepository flightRepository;

    private final SeatRepository seatRepository;

    private final FlightValidator flightValidator;

    @Override
    public List<Flight> getAll() {
        List<Flight> flights = flightRepository.findAll();
        for (Flight flight : flights) {
            flight.setSeats(seatRepository.findAllByFlightIdAndSeatStatus(flight.getId(), SeatStatus.AVAILABLE));
        }
        return flights;
    }

    @Override
    public void createFlight(Flight flight) {
        this.flightValidator.isValidToCreate(flight);
        flight.setCreationDate(System.currentTimeMillis());
        Flight f = flightRepository.save(flight);
        logger.info("flight id: " + f);

        if (flight.getSeats().size() > 0) {
            for (Seat seat : flight.getSeats()) {
                Seat newSeat = new Seat();
                newSeat.setSeatStatus(seat.getSeatStatus());
                newSeat.setPrice(seat.getPrice());
                newSeat.setFlight(f);
                seatRepository.save(newSeat);
            }
        }
    }

    @Override
    public void updateFlight(Flight flight) {
        this.flightValidator.isValidToEdit(flight);

        Flight updatableFlight = flightRepository.findById(flight.getId()).orElse(null);
        if (updatableFlight != null) {
            updatableFlight.setName(flight.getName());
            updatableFlight.setDescription(flight.getDescription());
            for (Seat seat : flight.getSeats()) {
                Seat changed = seatRepository.findById(seat.getId()).orElse(null);
                if (changed != null && flight.getSeats().contains(changed)) {
                    changed.setFlight(flight);
                    changed.setId(seat.getId());
                    changed.setPrice(seat.getPrice());
                    changed.setSeatStatus(seat.getSeatStatus());
                    seatRepository.save(changed);
                }
            }
            flightRepository.save(updatableFlight);
        }
    }

    @Override
    public void removeFlight(Long flightId) {
        boolean existsById = flightRepository.existsById(flightId);
        if (existsById) {
            flightRepository.deleteById(flightId);
        }
    }
}
