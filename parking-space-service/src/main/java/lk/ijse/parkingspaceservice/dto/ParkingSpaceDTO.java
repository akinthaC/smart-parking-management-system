package lk.ijse.parkingspaceservice.dto;

import java.time.LocalDateTime;

public class ParkingSpaceDTO {
    private Long id;
    private String location;
    private boolean available;
    private String status;


    public ParkingSpaceDTO() {
    }

    public ParkingSpaceDTO(Long id, String location, boolean available, String status) {
        this.id = id;
        this.location = location;
        this.available = available;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
