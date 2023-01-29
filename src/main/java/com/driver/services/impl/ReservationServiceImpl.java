package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {
        //Reserve a spot in the given parkingLot such that the total price is minimum.
        // Note that the price per hour for each spot is different
        //Note that the vehicle can only be parked in a spot having a type equal to or larger than given vehicle
        //If parkingLot is not found, user is not found, or no spot is available, throw "Cannot make reservation" exception.

        User user = userRepository3.findById(userId).get();
        ParkingLot parkingLot = parkingLotRepository3.findById(parkingLotId).get();
        //List<Spot> spotList = parkingLot.getSpotList();

        Spot finalSpot = null;
        int bill = Integer.MAX_VALUE;
/*
        if(user == null && parkingLot == null)
        {
            throw new Exception("Cannot make reservation");
        }

 */
        List<Spot> spotList = parkingLot.getSpotList();
        if(spotList == null) throw new Exception("Cannot make reservation");

        List<Spot> availableSpotsList = new ArrayList<>();

            for(Spot spot: spotList)
            {
                if(spot.getOccupied() == false)
                {

                   int vehicle = Integer.MAX_VALUE;

                   String str = spot.getSpotType().toString();
                   char ch = str.charAt(0);
                   if(ch == 'T') vehicle = 2;
                   else if(ch == 'F') vehicle = 4;
                   else if(ch == 'O') vehicle = 5;


                    /*
                    if (spot.getSpotType() == SpotType.TWO_WHEELER)
                        vehicle = 2;
                    else if (spot.getSpotType() == SpotType.FOUR_WHEELER)
                        vehicle = 4;

                     */


                   if(vehicle >= numberOfWheels)
                       availableSpotsList.add(spot);
                }
            }
           // if(availableSpotsList.isEmpty()) throw new Exception("Cannot make reservation");

            for(Spot availableSpot: availableSpotsList)
            {
                int currentBill = availableSpot.getPricePerHour() * timeInHours;
                if(bill > currentBill)
                {
                    bill = currentBill;
                    finalSpot = availableSpot;
                }
            }

        if(finalSpot == null) {
            throw new Exception("Cannot make reservation");
        }
            Reservation reservation = new Reservation();
            reservation.setSpot(finalSpot);
            reservation.setUser(user);
            reservation.setNumberOfHours(timeInHours);

            List<Reservation> reservationList = finalSpot.getReservationList();
            reservationList.add(reservation);

            finalSpot.setReservationList(reservationList);
            user.setReservationList(reservationList);

            finalSpot.setOccupied(Boolean.TRUE);

        userRepository3.save(user);
        spotRepository3.save(finalSpot);

        return reservation;
    }
}
