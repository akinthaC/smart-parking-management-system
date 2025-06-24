package lk.ijse.parkingspaceservice.controller;


import lk.ijse.parkingspaceservice.dto.ParkingLogDTO;
import lk.ijse.parkingspaceservice.dto.ParkingSpaceDTO;
import lk.ijse.parkingspaceservice.entity.ParkingLog;
import lk.ijse.parkingspaceservice.response.ApiResponse;
import lk.ijse.parkingspaceservice.service.ParkingLogService;
import lk.ijse.parkingspaceservice.service.ParkingSpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/parking-spaces")
public class ParkingSpaceController {

    @Autowired
    private ParkingSpaceService service;

    @Autowired
    private ParkingLogService parkingLogService;


    @PostMapping("/add-parking-space")
    public ResponseEntity<ApiResponse<ParkingSpaceDTO>> add(@RequestBody ParkingSpaceDTO dto) {
        ParkingSpaceDTO saved = service.save(dto);
        ApiResponse<ParkingSpaceDTO> response = new ApiResponse<>("Parking space saved successfully", saved);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getAll-parking-spaces")
    public ResponseEntity<List<ParkingSpaceDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ParkingSpaceDTO> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        return ResponseEntity.ok(service.updateStatus(id, status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParkingSpaceDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/available")
    public ResponseEntity<?> getAvailableByStatus() {
        List<ParkingSpaceDTO> availableSpaces = service.getAvailableByStatus();

        if (availableSpaces.isEmpty()) {
            return ResponseEntity.ok(new ApiResponse<>("No available parking spaces", null));
        }

        return ResponseEntity.ok(new ApiResponse<>("Available parking spaces fetched successfully", availableSpaces));
    }


    //------------------------------------parking space OCCUPIED part----------------------------------------------


    @PostMapping("/reserved-vehicle")
    public ResponseEntity<?> vehicleReserved(@RequestBody ParkingLogDTO dto) {
        return ResponseEntity.ok(parkingLogService.reserveVehicle(dto));
    }

    @GetMapping("/checkout")
    public ResponseEntity<?> vehicleCheckout(
            @RequestParam String email,
            @RequestParam String vehicleNumber) {
        return ResponseEntity.ok(parkingLogService.vehicleCheckout(email, vehicleNumber));
    }


    @GetMapping("/last-completed")
    public ResponseEntity<ParkingLogDTO> getLastCompletedLog(
            @RequestParam String email,
            @RequestParam String vehicleNumber
    ) {
        try {
            ParkingLogDTO dto = parkingLogService.getLastCompletedLog(email, vehicleNumber);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PatchMapping("/mark-paid")
    public ResponseEntity<String> markAsPaid(
            @RequestParam String email,
            @RequestParam String vehicleNumber) {

        boolean updated = parkingLogService.markAsPaid(email, vehicleNumber);

        if (!updated) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("❌ Parking log not found for email: " + email + " and vehicle: " + vehicleNumber);
        }

        return ResponseEntity.ok("✅ Parking log marked as paid");
    }





}
