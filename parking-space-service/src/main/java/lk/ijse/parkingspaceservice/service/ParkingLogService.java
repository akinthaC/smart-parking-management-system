package lk.ijse.parkingspaceservice.service;

import lk.ijse.parkingspaceservice.dto.ParkingLogDTO;
import lk.ijse.parkingspaceservice.response.ApiResponse;

import java.util.List;

public interface ParkingLogService {
    ParkingLogDTO save(ParkingLogDTO dto);
    List<ParkingLogDTO> getAll();
    ParkingLogDTO findById(Long id);
    ApiResponse<?> reserveVehicle(ParkingLogDTO dto);

    ApiResponse<?> vehicleCheckout(String email, String vehicleNumber);
    ParkingLogDTO getLastCompletedLog(String email, String vehicleNumber);

    boolean markAsPaid(String email, String vehicleNumber);
}
