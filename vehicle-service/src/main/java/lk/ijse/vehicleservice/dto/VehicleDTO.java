package lk.ijse.vehicleservice.dto;

import lombok.Builder;

@Builder
public class VehicleDTO {

    private Long id;
    private String plateNumber;
    private String type;
    private String color;
    private Long userId;


    public VehicleDTO() {
    }


    public VehicleDTO(Long id, String plateNumber, String type, String color, Long userId) {
        this.id = id;
        this.plateNumber = plateNumber;
        this.type = type;
        this.color = color;
        this.userId = userId;
    }

    // ✅ Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


    @Override
    public String toString() {
        return "VehicleDTO{" +
                "id=" + id +
                ", plateNumber='" + plateNumber + '\'' +
                ", type='" + type + '\'' +
                ", color='" + color + '\'' +
                ", userId=" + userId +
                '}';
    }
}
