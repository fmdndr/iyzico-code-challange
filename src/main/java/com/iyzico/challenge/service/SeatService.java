package com.iyzico.challenge.service;

import com.iyzico.challenge.entity.Seat;

import java.util.List;

public interface SeatService {
    String buyTicket(Long seatId);

    List<Seat> getAllSeatsByFlightId(Long flightId);

    Seat addSeatToFlight(Seat seat, Long flightId);

    void removeSeatFromFlight(Long seatId);

    Seat updateSeat(Seat seat);
}
