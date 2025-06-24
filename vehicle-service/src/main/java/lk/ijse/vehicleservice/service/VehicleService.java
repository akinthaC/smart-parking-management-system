package lk.ijse.vehicleservice.service;



import lk.ijse.vehicleservice.dto.VehicleDTO;
import lk.ijse.vehicleservice.response.VehicleResponse;

import java.util.List;
import java.util.Optional;

public interface VehicleService {
    VehicleResponse save(VehicleDTO dto);

    List<VehicleDTO> getAll();
    Optional<VehicleDTO> getById(Long id);
    VehicleDTO update(Long id, VehicleDTO dto);
    void delete(Long id);
    List<VehicleDTO> getByUserId(Long userId);

    boolean isVehicleRegistered(String number);
}
