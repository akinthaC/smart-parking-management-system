package lk.ijse.parkingspaceservice.repository;

import lk.ijse.parkingspaceservice.entity.ParkingLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParkingLogRepository extends JpaRepository<ParkingLog, Long> {
    List<ParkingLog> findByEmailAndVehicleNumberAndIsActiveTrue(String email, String vehicleNumber);

    Optional<ParkingLog> findTopByEmailAndVehicleNumberAndIsActiveFalseOrderByCheckOutTimeDesc(String email, String vehicleNumber);

    Optional<ParkingLog> findTopByEmailAndVehicleNumberOrderByCheckOutTimeDesc(String email, String vehicleNumber);
}
