package com.iyzico.challenge.service.implementation;

import com.iyzico.challenge.constants.SeatStatus;
import com.iyzico.challenge.entity.Flight;
import com.iyzico.challenge.entity.Payment;
import com.iyzico.challenge.entity.Seat;
import com.iyzico.challenge.repository.FlightRepository;
import com.iyzico.challenge.repository.PaymentRepository;
import com.iyzico.challenge.repository.SeatRepository;
import com.iyzico.challenge.service.BankPaymentRequest;
import com.iyzico.challenge.service.BankPaymentResponse;
import com.iyzico.challenge.service.BankService;
import com.iyzico.challenge.service.SeatService;
import com.iyzico.challenge.validator.FlightValidator;
import com.iyzico.challenge.validator.SeatValidator;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SeatServiceImp implements SeatService {
    private final SeatRepository seatRepository;
    private final FlightRepository flightRepository;
    private final PaymentRepository paymentRepository;
    private final Logger logger = LoggerFactory.getLogger(SeatServiceImp.class);
    private final SeatValidator seatValidator;
    private final FlightValidator flightValidator;

    @Override
    @Synchronized
    public String buyTicket(Long seatId) {
        logger.info("seatId: " + seatId);
        Seat seat = seatRepository.findById(seatId).orElse(null);

        seatValidator.isValidToBuy(Objects.requireNonNull(seat));
        BankPaymentRequest bankPaymentRequest = new BankPaymentRequest();
        bankPaymentRequest.setPrice(BigDecimal.valueOf(seat.getPrice()));
        BankService bankService = new BankService();
        BankPaymentResponse status = bankService.pay(bankPaymentRequest);

        if (status.getResultCode().equals(HttpStatus.OK.toString())) {
            Payment payment = new Payment();
            payment.setBankResponse(status.getResultCode());
            payment.setPrice(BigDecimal.valueOf(seat.getPrice()));
            paymentRepository.save(payment);
            seat.setSeatStatus(SeatStatus.SOLD);
            seatRepository.save(seat);
            return "Seat has been sold successfully and your seat is : " + seat.getId() + " price " + seat.getPrice();
        } else {
            return "Something happened while processing your payment or seat has been sold another customer.";
        }
    }

    @Override
    public List<Seat> getAllSeatsByFlightId(Long flightId) {
        logger.info("flightId: " + flightId);
        flightValidator.isExistsByFlightId(flightId);
        return seatRepository.findAllByFlightId(flightId);
    }

    @Override
    public Seat addSeatToFlight(Seat seat, Long flightId) {
        logger.info("seat.getId(): " + seat.getId() + "|flightId: " + flightId);
        flightValidator.isExistsByFlightId(flightId);

        Flight flight = flightRepository.findById(flightId).orElse(null);
        if (flight != null) {
            seat.setFlight(flight);
            seatRepository.save(seat);
        }
        return seat;
    }

    @Override
    public void removeSeatFromFlight(Long seatId) {
        boolean isSeatExist = seatRepository.existsById(seatId);
        logger.info("seatId: " + seatId + "isSeatExists: " + isSeatExist);
        if (isSeatExist) {
            seatRepository.deleteById(seatId);
        }
    }

    @Override
    public Seat updateSeat(Seat seat) {
        Seat existsById = seatRepository.findById(seat.getId()).orElse(null);
        logger.info("seat.getId()" + seat.getId() + "existsById : " + existsById);

        if (seatRepository.existsById(seat.getId())) {
            assert existsById != null;
            seat.setId(existsById.getId());
            seat.setFlight(existsById.getFlight());
            return seatRepository.save(seat);
        }
        return seat;
    }
}
