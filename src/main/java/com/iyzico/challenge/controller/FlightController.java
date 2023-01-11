package com.iyzico.challenge.controller;


import com.iyzico.challenge.entity.Flight;
import com.iyzico.challenge.service.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/flights")
@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class FlightController {
    private final FlightService flightService;

    @GetMapping
    public ResponseEntity<List<Flight>> getAll() {
        return ResponseEntity.ok(flightService.getAll());
    }

    @PostMapping
    public ResponseEntity<String> createFlight(@RequestBody Flight flight) {
        flightService.createFlight(flight);
        return ResponseEntity.ok("Flight has been created");
    }

    @PutMapping
    public ResponseEntity<String> updateFlight(@RequestBody Flight flight) {
        flightService.updateFlight(flight);
        return ResponseEntity.ok("Flight has been updated");
    }

    @DeleteMapping("/{flightId}")
    public ResponseEntity<String> removeFlight(@PathVariable("flightId") Long flightId) {
        flightService.removeFlight(flightId);
        return ResponseEntity.ok("Flight has been removed");
    }

}
