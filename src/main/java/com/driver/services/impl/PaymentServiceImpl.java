package com.driver.services.impl;

import com.driver.model.Payment;
import com.driver.model.PaymentMode;
import com.driver.model.Reservation;
import com.driver.model.Spot;
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

        Reservation reservation = reservationRepository2.findById(reservationId).get();
        Spot spot = reservation.getSpot();

        Payment payment = reservation.getPayment();

        if(! isPaymentCompleted(reservationId))
        {
            int bill = spot.getPricePerHour() * reservation.getNumberOfHours();
            if(amountSent < bill)
            {
                payment.setPaymentCompleted(Boolean.FALSE);
                throw new Exception("Insufficient Amount");
            }

            if(mode.equals("cash") || mode.equals("card") || mode.equals("upi"))
            {
                if(mode.equals("cash")) payment.setPaymentMode(PaymentMode.CASH);
                else if(mode.equals("card")) payment.setPaymentMode(PaymentMode.CARD);
                else payment.setPaymentMode(PaymentMode.UPI);
            }
            else
            {
                payment.setPaymentCompleted(Boolean.FALSE);
                throw new Exception("Payment mode not detected");
            }

            payment.setReservation(reservation);
            payment.setPaymentCompleted(Boolean.TRUE);

            reservation.setPayment(payment);
            spot.setOccupied(Boolean.FALSE);

        }

        paymentRepository2.save(payment);

        return payment;
    }
    public Boolean isPaymentCompleted(Integer reservationId)
    {
        Reservation reservation = reservationRepository2.findById(reservationId).get();
        Payment payment = reservation.getPayment();
        if(payment.getPaymentCompleted()) return true;
        else return false;
    }
}
