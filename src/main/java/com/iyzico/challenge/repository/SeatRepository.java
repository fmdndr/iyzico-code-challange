package com.iyzico.challenge.repository;

import com.iyzico.challenge.constants.SeatStatus;
import com.iyzico.challenge.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findAllByFlightId(Long flightId);

    List<Seat> findAllByFlightIdAndSeatStatus(Long flightId, SeatStatus seatStatus);

    Seat findByFlightId(Long flightId);
}
