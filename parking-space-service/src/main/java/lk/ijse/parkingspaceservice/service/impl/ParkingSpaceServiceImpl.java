package lk.ijse.parkingspaceservice.service.impl;

import lk.ijse.parkingspaceservice.dto.ParkingSpaceDTO;
import lk.ijse.parkingspaceservice.entity.ParkingSpace;
import lk.ijse.parkingspaceservice.repository.ParkingSpaceRepository;
import lk.ijse.parkingspaceservice.service.ParkingSpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParkingSpaceServiceImpl implements ParkingSpaceService {

    @Autowired
    private ParkingSpaceRepository repository;

    @Override
    public ParkingSpaceDTO save(ParkingSpaceDTO dto) {
        ParkingSpace entity = toEntity(dto);
        return toDTO(repository.save(entity));
    }

    @Override
    public List<ParkingSpaceDTO> getAll() {
        return repository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ParkingSpaceDTO updateStatus(Long id, String status) {
        ParkingSpace space = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parking space not found"));

        space.setStatus(status);
        space.setAvailable("AVAILABLE".equalsIgnoreCase(status));

        return toDTO(repository.save(space));
    }

    @Override
    public ParkingSpaceDTO findById(Long id) {
        return toDTO(repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parking space not found")));
    }

    @Override
    public List<ParkingSpaceDTO> getAvailableByStatus() {
        return repository.findByStatusIgnoreCase("AVAILABLE")
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }


    private ParkingSpaceDTO toDTO(ParkingSpace entity) {
        ParkingSpaceDTO dto = new ParkingSpaceDTO();
        dto.setId(entity.getId());
        dto.setLocation(entity.getLocation());
        dto.setAvailable(entity.isAvailable());
        dto.setStatus(entity.getStatus());
        return dto;
    }

    private ParkingSpace toEntity(ParkingSpaceDTO dto) {
        ParkingSpace entity = new ParkingSpace();
        entity.setId(dto.getId());
        entity.setLocation(dto.getLocation());
        entity.setAvailable(dto.isAvailable());
        entity.setStatus(dto.getStatus());

        return entity;
    }
}
