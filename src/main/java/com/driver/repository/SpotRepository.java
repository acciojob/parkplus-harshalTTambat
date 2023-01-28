package com.driver.repository;

import com.driver.model.ParkingLot;
import com.driver.model.Spot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;



@Repository
public interface SpotRepository extends JpaRepository<Spot, Integer>{

    @Modifying
    @Transactional
    @Query(value = "update Spot s set s.pricePerHour =: pricePerHour where s.id =: spotId and s.parkingLot.id =: parkingLotId", nativeQuery = true)
    Spot updateSpot(int parkingLotId, int spotId, int pricePerHour);
}
