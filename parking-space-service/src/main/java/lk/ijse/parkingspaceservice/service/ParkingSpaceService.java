package lk.ijse.parkingspaceservice.service;


import lk.ijse.parkingspaceservice.dto.ParkingSpaceDTO;

import java.util.List;

public interface ParkingSpaceService {
    ParkingSpaceDTO save(ParkingSpaceDTO dto);
    List<ParkingSpaceDTO> getAll();
    ParkingSpaceDTO updateStatus(Long id, String status);
    ParkingSpaceDTO findById(Long id);
    List<ParkingSpaceDTO> getAvailableByStatus();

}
