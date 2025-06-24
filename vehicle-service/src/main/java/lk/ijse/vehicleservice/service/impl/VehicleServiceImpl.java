package lk.ijse.vehicleservice.service.impl;

import lk.ijse.vehicleservice.config.AppConfig;
import lk.ijse.vehicleservice.dto.VehicleDTO;
import lk.ijse.vehicleservice.entity.Vehicle;
import lk.ijse.vehicleservice.repository.VehicleRepository;
import lk.ijse.vehicleservice.response.VehicleResponse;
import lk.ijse.vehicleservice.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VehicleServiceImpl implements VehicleService {

    @Autowired
    private VehicleRepository repository;

    @Autowired
    private RestTemplate restTemplate;
    @Override
    public VehicleResponse save(VehicleDTO dto) {
        String userServiceUrl = "http://localhost:8081/user-service/api/v1/users/" + dto.getUserId();

        try {
            // Check if vehicle already exists
            Optional<Vehicle> existing = repository.findByPlateNumber(dto.getPlateNumber());
            if (existing.isPresent()) {
                return new VehicleResponse(false, "This vehicle is already registered.", null);
            }

            // Check if user exists
            ResponseEntity<Object> response = restTemplate.getForEntity(userServiceUrl, Object.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Vehicle savedVehicle = repository.save(dtoToEntity(dto));
                return new VehicleResponse(true, "Vehicle saved successfully.", entityToDto(savedVehicle));
            } else {
                return new VehicleResponse(false, "User not found: " + dto.getUserId(), null);
            }
        } catch (HttpClientErrorException.NotFound e) {
            return new VehicleResponse(false, "User not found: " + dto.getUserId(), null);
        } catch (Exception e) {
            return new VehicleResponse(false, "Error: " + e.getMessage(), null);
        }
    }




    @Override
    public List<VehicleDTO> getAll() {
        return repository.findAll().stream().map(this::entityToDto).collect(Collectors.toList());
    }

    @Override
    public Optional<VehicleDTO> getById(Long id) {
        return repository.findById(id).map(this::entityToDto);
    }

    @Override
    public VehicleDTO update(Long id, VehicleDTO dto) {
        Vehicle vehicle = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        vehicle.setPlateNumber(dto.getPlateNumber());
        vehicle.setType(dto.getType());
        vehicle.setColor(dto.getColor());
        vehicle.setUserId(dto.getUserId());

        return entityToDto(repository.save(vehicle));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<VehicleDTO> getByUserId(Long userId) {
        return repository.findByUserId(userId)
                .stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isVehicleRegistered(String plateNumber) {
        return repository.existsByPlateNumber(plateNumber);
    }

    // ✅ Mapping methods (no builder used)
    private Vehicle dtoToEntity(VehicleDTO dto) {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(dto.getId());
        vehicle.setPlateNumber(dto.getPlateNumber());
        vehicle.setType(dto.getType());
        vehicle.setColor(dto.getColor());
        vehicle.setUserId(dto.getUserId());
        return vehicle;
    }

    private VehicleDTO entityToDto(Vehicle vehicle) {
        VehicleDTO dto = new VehicleDTO();
        dto.setId(vehicle.getId());
        dto.setPlateNumber(vehicle.getPlateNumber());
        dto.setType(vehicle.getType());
        dto.setColor(vehicle.getColor());
        dto.setUserId(vehicle.getUserId());
        return dto;
    }
}
