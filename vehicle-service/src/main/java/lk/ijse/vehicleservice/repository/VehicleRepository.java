package lk.ijse.vehicleservice.repository;


import lk.ijse.vehicleservice.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findByUserId(Long userId);
    Optional<Vehicle> findByPlateNumber(String numberPlate);

    boolean existsByPlateNumber(String plateNumber);
}
