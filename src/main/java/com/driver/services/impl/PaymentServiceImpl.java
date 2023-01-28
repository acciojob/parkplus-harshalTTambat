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

        PaymentMode paymentMode;
        Reservation reservation=reservationRepository2.findById(reservationId).get();
        Payment payment = reservation.getPayment();
        if(payment == null) payment = new Payment();
        if(payment.isPaymentCompleted() == true) return payment;

        if(mode.equalsIgnoreCase("cash")){
            paymentMode=PaymentMode.CASH;
        }
        else if(mode.equalsIgnoreCase("card")){
            paymentMode=PaymentMode.CARD;
        }
        else if(mode.equalsIgnoreCase("upi")){
            paymentMode=PaymentMode.UPI;
        }
        else{
            payment.setPaymentCompleted(Boolean.FALSE);
            throw new Exception("Payment mode not detected");
        }
        int bill=reservation.getSpot().getPricePerHour()*reservation.getNumberOfHours();
        if(amountSent<bill){
            payment.setPaymentCompleted(Boolean.FALSE);
            throw  new Exception("Insufficient Amount");
        }

        payment.setPaymentCompleted(Boolean.TRUE);
        payment.setPaymentMode(paymentMode);
        payment.setReservation(reservation);

        reservation.setPayment(payment);
        reservation.getSpot().setOccupied(false);
        reservationRepository2.save(reservation);
        return payment;
    }

}
