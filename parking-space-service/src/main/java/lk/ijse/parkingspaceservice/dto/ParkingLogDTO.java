package lk.ijse.parkingspaceservice.dto;

import java.time.LocalDateTime;

public class ParkingLogDTO {
    private Long id;
    private String email;
    private String vehicleNumber;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private Long parkingSpaceId;
    private Boolean isActive;
    private double totalFee;
    private long durationInMinutes;

    private boolean isPaid;

    public ParkingLogDTO() {}

    public ParkingLogDTO(Long id, String email, String vehicleNumber, LocalDateTime checkInTime, LocalDateTime checkOutTime, Long parkingSpaceId, Boolean isActive, double totalFee, long durationInMinutes) {
        this.id = id;
        this.email = email;
        this.vehicleNumber = vehicleNumber;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.parkingSpaceId = parkingSpaceId;
        this.isActive = isActive;
        this.totalFee = totalFee;
        this.durationInMinutes = durationInMinutes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public LocalDateTime getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(LocalDateTime checkInTime) {
        this.checkInTime = checkInTime;
    }

    public LocalDateTime getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(LocalDateTime checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public Long getParkingSpaceId() {
        return parkingSpaceId;
    }

    public void setParkingSpaceId(Long parkingSpaceId) {
        this.parkingSpaceId = parkingSpaceId;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public double getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(double totalFee) {
        this.totalFee = totalFee;
    }

    public long getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(long durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }
}

