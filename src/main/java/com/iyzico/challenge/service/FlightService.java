package com.iyzico.challenge.service;

import com.iyzico.challenge.entity.Flight;

import java.util.List;

public interface FlightService {
    List<Flight> getAll();

    void createFlight(Flight flight);

    void updateFlight(Flight flight);

    void removeFlight(Long flightId);
}
