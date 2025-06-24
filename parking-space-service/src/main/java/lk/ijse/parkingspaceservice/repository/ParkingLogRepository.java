package lk.ijse.parkingspaceservice.repository;

import lk.ijse.parkingspaceservice.entity.ParkingLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParkingLogRepository extends JpaRepository<ParkingLog, Long> {
    List<ParkingLog> findByEmailAndVehicleNumberAndIsActiveTrue(String email, String vehicleNumber);
}
