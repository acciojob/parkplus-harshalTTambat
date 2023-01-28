package com.driver.services.impl;

import com.driver.model.ParkingLot;
import com.driver.model.Reservation;
import com.driver.model.Spot;
import com.driver.model.SpotType;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.PaymentRepository;
import com.driver.repository.SpotRepository;
import com.driver.services.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    @Autowired
    ParkingLotRepository parkingLotRepository1;
    @Autowired
    SpotRepository spotRepository1;


    @Override
    public ParkingLot addParkingLot(String name, String address) {
        ParkingLot parkingLot = new ParkingLot(name,address);
        List<Spot> spotList = new ArrayList<>();
        parkingLot.setSpotList(spotList);
        parkingLotRepository1.save(parkingLot);
        return parkingLot;
    }

    @Override
    public Spot addSpot(int parkingLotId, Integer numberOfWheels, Integer pricePerHour) {
        //creating new spot
        Spot spot = new Spot();

        spot.setOccupied(Boolean.FALSE);
        spot.setPricePerHour(pricePerHour);

        if(numberOfWheels == 2)
        {
            spot.setSpotType(SpotType.TWO_WHEELER);
        }
        if(numberOfWheels == 4)
        {
            spot.setSpotType(SpotType.FOUR_WHEELER);
        }
        if(numberOfWheels > 4)
        {
            spot.setSpotType(SpotType.OTHERS);
        }
        //Bidirectional part
        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();
        List<Spot> spotList = parkingLot.getSpotList();
        spotList.add(spot);
        parkingLot.setSpotList(spotList);

        spot.setParkingLot(parkingLot);

        List<Reservation> reservationList = new ArrayList<>();
        spot.setReservationList(reservationList);

        spotRepository1.save(spot);
        return spot;
    }

    @Override
    public void deleteSpot(int spotId) {
        Spot spot = spotRepository1.findById(spotId).get();
        ParkingLot parkingLot = spot.getParkingLot();
        List<Spot> spotList = parkingLot.getSpotList();
        spotList.remove(spot);

        spotRepository1.deleteById(spotId);
        parkingLotRepository1.save(parkingLot);
    }

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour) {
        return spotRepository1.updateSpot(parkingLotId,spotId,pricePerHour);
    }

    @Override
    public void deleteParkingLot(int parkingLotId) {
        parkingLotRepository1.deleteById(parkingLotId);
    }
}
