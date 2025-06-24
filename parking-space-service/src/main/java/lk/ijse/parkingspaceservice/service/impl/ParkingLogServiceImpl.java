package lk.ijse.parkingspaceservice.service.impl;

import lk.ijse.parkingspaceservice.dto.ParkingLogDTO;
import lk.ijse.parkingspaceservice.entity.ParkingLog;
import lk.ijse.parkingspaceservice.entity.ParkingSpace;
import lk.ijse.parkingspaceservice.repository.ParkingLogRepository;
import lk.ijse.parkingspaceservice.repository.ParkingSpaceRepository;
import lk.ijse.parkingspaceservice.response.ApiResponse;
import lk.ijse.parkingspaceservice.service.ParkingLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParkingLogServiceImpl implements ParkingLogService {

    @Autowired
    private ParkingLogRepository logRepository;

    @Autowired
    private ParkingSpaceRepository spaceRepository;

    @Autowired
    private ParkingLogRepository parkingLogRepository;

    @Autowired
    private RestTemplate restTemplate;


    @Override
    public ParkingLogDTO save(ParkingLogDTO dto) {
        ParkingLog log = new ParkingLog();
        log.setEmail(dto.getEmail());
        log.setVehicleNumber(dto.getVehicleNumber());
        log.setCheckInTime(dto.getCheckInTime());
        log.setCheckOutTime(dto.getCheckOutTime());

        ParkingSpace space = spaceRepository.findById(dto.getParkingSpaceId())
                .orElseThrow(() -> new RuntimeException("Parking space not found"));
        log.setParkingSpace(space);

        return toDTO(logRepository.save(log));
    }

    @Override
    public List<ParkingLogDTO> getAll() {
        return logRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ParkingLogDTO findById(Long id) {
        return toDTO(logRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parking log not found")));
    }

    private ParkingLogDTO toDTO(ParkingLog log) {
        ParkingLogDTO dto = new ParkingLogDTO();
        dto.setId(log.getId());
        dto.setEmail(log.getEmail());
        dto.setVehicleNumber(log.getVehicleNumber());
        dto.setCheckInTime(log.getCheckInTime());
        dto.setCheckOutTime(log.getCheckOutTime());
        dto.setParkingSpaceId(log.getParkingSpace().getId());
        dto.setActive(log.getActive());
        return dto;
    }

    @Override
    public ApiResponse<?> reserveVehicle(ParkingLogDTO dto) {
        // Step 1: Check available parking spaces
        List<ParkingSpace> availableSpaces = spaceRepository.findByStatusIgnoreCase("AVAILABLE");
        if (availableSpaces.isEmpty()) {
            return new ApiResponse<>("No available parking spaces", null);
        }

        // Step 2: Check if user and vehicle already reserved
        String userServiceUrl = "http://localhost:8081/user-service/api/v1/users/email/" + dto.getEmail();
        String vehicleServiceUrl = "http://localhost:8083/vehicle-service/api/v1/vehicles/plate/" + dto.getVehicleNumber();

        try {
            ResponseEntity<Boolean> userExistsResponse = restTemplate.getForEntity(userServiceUrl, Boolean.class);
            if (userExistsResponse.getBody() == null || !userExistsResponse.getBody()) {
                return new ApiResponse<>("User email not registered please register firstly  ", null);
            }

            ResponseEntity<Boolean> vehicleExistsResponse = restTemplate.getForEntity(vehicleServiceUrl, Boolean.class);
            if (vehicleExistsResponse.getBody() == null || !vehicleExistsResponse.getBody()) {
                return new ApiResponse<>("Vehicle number plate not registered please register firstly", null);
            }

        } catch (Exception e) {
            return new ApiResponse<>("Failed to validate user or vehicle: " + e.getMessage(), null);
        }

        List<ParkingLog> activeLogs = parkingLogRepository
                .findByEmailAndVehicleNumberAndIsActiveTrue(dto.getEmail(), dto.getVehicleNumber());

        if (!activeLogs.isEmpty()) {
            return new ApiResponse<>("You have unpaid parking. Please pay it before making another reservation.", null);
        }



        ParkingSpace selectedSpace = availableSpaces.get(0);
        selectedSpace.setAvailable(false);
        selectedSpace.setStatus("OCCUPIED");
        spaceRepository.save(selectedSpace);


        ParkingLog log = new ParkingLog();
        log.setEmail(dto.getEmail());
        log.setVehicleNumber(dto.getVehicleNumber());
        log.setCheckInTime(LocalDateTime.now());
        log.setCheckOutTime(null);
        log.setParkingSpace(selectedSpace);
        log.setActive(true);
        ParkingLog savedLog = parkingLogRepository.save(log);


        ParkingLogDTO responseDTO = toDTO(savedLog);
        return new ApiResponse<>("Vehicle reserved successfully", responseDTO);
    }



    public ApiResponse<?> vehicleCheckout(String email, String vehicleNumber) {
        // Step 1: Find active parking log
        ParkingLog log = parkingLogRepository
                .findByEmailAndVehicleNumberAndIsActiveTrue(email, vehicleNumber)
                .stream()
                .findFirst()
                .orElse(null);

        if (log == null) {
            return new ApiResponse<>("No active reservation found for this vehicle", null);
        }

        // Step 2: Update checkout details
        log.setCheckOutTime(LocalDateTime.now());
        log.setActive(false);
        parkingLogRepository.save(log);

        // Step 3: Mark parking space as available
        ParkingSpace space = log.getParkingSpace();
        space.setAvailable(true);
        space.setStatus("AVAILABLE");
        spaceRepository.save(space);

        // Step 4: Return updated log
        ParkingLogDTO dto = toDTO(log);
        return new ApiResponse<>("Vehicle checked out successfully", dto);
    }



}
