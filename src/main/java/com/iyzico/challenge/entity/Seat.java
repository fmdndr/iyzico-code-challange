package com.iyzico.challenge.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.iyzico.challenge.constants.SeatStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Enumerated(EnumType.STRING)
    private SeatStatus seatStatus = SeatStatus.AVAILABLE;

    private double price = 0.0;
    @JsonIgnore
    @ManyToOne(targetEntity = Flight.class, fetch = FetchType.LAZY)
    private Flight flight;

}
