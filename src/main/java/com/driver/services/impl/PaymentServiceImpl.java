package com.driver.services.impl;

import com.driver.model.Payment;
import com.driver.model.PaymentMode;
import com.driver.model.Reservation;
import com.driver.model.Spot;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.PaymentRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    ReservationRepository reservationRepository2;
    @Autowired
    PaymentRepository paymentRepository2;

    //Attempt a payment of amountSent for reservationId using the given mode ("cASh", "card", or "upi")
    //If the amountSent is less than bill, throw "Insufficient Amount" exception, otherwise update payment attributes
    //If the mode contains a string other than "cash", "card", or "upi" (any character in uppercase or lowercase), throw "Payment mode not detected" exception.
    //Note that the reservationId always exists
    @Override
    public Payment pay(Integer reservationId, int amountSent, String mode) throws Exception {

        Reservation reservation=reservationRepository2.findById(reservationId).get();
        Spot spot=reservation.getSpot();

        int amount=spot.getPricePerHour()*reservation.getNumberOfHours();
        if(amountSent<amount){
            throw new Exception("Insufficient Amount");

        }

        Payment payment=new Payment();

        String Mode=mode.toUpperCase();

        if(Mode.equals("CASH")){
            payment.setPaymentMode(PaymentMode.CASH);
        } else if (Mode.equals("CARD")) {
            payment.setPaymentMode(PaymentMode.CARD);
        } else if (Mode.equals("UPI")) {
            payment.setPaymentMode(PaymentMode.UPI);
        }else{
            payment.setPaymentCompleted(false);
            throw new Exception("Payment mode not detected");
        }

        payment.setPaymentCompleted(true);
        payment.setReservation(reservation);
        reservation.setPayment(payment);

        reservationRepository2.save(reservation);

        return payment;

    }
}
