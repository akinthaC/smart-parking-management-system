package lk.ijse.vehicleservice.controller;


import lk.ijse.vehicleservice.dto.VehicleDTO;
import lk.ijse.vehicleservice.response.VehicleResponse;
import lk.ijse.vehicleservice.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/vehicles")
public class VehicleController {

    @Autowired
    private VehicleService service;

    @PostMapping
    public ResponseEntity<VehicleResponse> register(@RequestBody VehicleDTO dto) {
        VehicleResponse response = service.save(dto);
        return ResponseEntity
                .status(response.isSuccess() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST)
                .body(response);
    }



    @GetMapping
    public List<VehicleDTO> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Optional<VehicleDTO> getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public VehicleDTO update(@PathVariable Long id, @RequestBody VehicleDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/user/{userId}")
    public List<VehicleDTO> getByUserId(@PathVariable Long userId) {
        return service.getByUserId(userId);
    }

    @GetMapping("/plate/{number}")
    public ResponseEntity<Boolean> isVehicleRegistered(@PathVariable String number) {
        boolean exists = service.isVehicleRegistered(number);
        return ResponseEntity.ok(exists);
    }
}
