package com.iyzico.challenge.controller;


import com.iyzico.challenge.entity.Seat;
import com.iyzico.challenge.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/seats")
@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class SeatController {
    private final SeatService seatService;

    @GetMapping("/seatsForExistsFlight/{flightId}")
    public ResponseEntity<List<Seat>> seatsForExistsFlight(@PathVariable("flightId") Long flightId) {
        return ResponseEntity.ok(seatService.getAllSeatsByFlightId(flightId));
    }

    @PostMapping("/addSeatToExistsFlight")
    public ResponseEntity<Seat> addSeatToFlight(@RequestBody Seat seat, @RequestParam("flightId") Long flightId) {
        return ResponseEntity.ok(seatService.addSeatToFlight(seat, flightId));
    }

    @PutMapping
    public ResponseEntity<Seat> updateSeat(@RequestBody Seat seat) {
        return ResponseEntity.ok(seatService.updateSeat(seat));
    }

    @PutMapping("/buyTicket/{seatId}")
    public ResponseEntity<String> buyTicket(@PathVariable("seatId") Long seatId) {
        return ResponseEntity.ok(seatService.buyTicket(seatId));
    }

    @DeleteMapping
    public ResponseEntity<String> removeSeatFromFlight(@RequestParam("seatId") Long seatId) {
        seatService.removeSeatFromFlight(seatId);
        return ResponseEntity.ok("Removed Successfully");
    }

}
