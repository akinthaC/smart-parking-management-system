package lk.ijse.paymentservice.dto;

public class PaymentDTO {
    private String email;
    private String vehicleNumber;
    private String method;
    private String status;
    private long durationInMinutes;
    private double totalFee;
    private MockCardDTO mockCardDTO;

    public PaymentDTO() {}

    public PaymentDTO(String email, String vehicleNumber, String method, String status, long durationInMinutes, double totalFee, MockCardDTO mockCardDTO) {
        this.email = email;
        this.vehicleNumber = vehicleNumber;
        this.method = method;
        this.status = status;
        this.durationInMinutes = durationInMinutes;
        this.totalFee = totalFee;
        this.mockCardDTO = mockCardDTO;
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

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(long durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    public double getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(double totalFee) {
        this.totalFee = totalFee;
    }

    public MockCardDTO getMockCardDTO() {
        return mockCardDTO;
    }

    public void setMockCardDTO(MockCardDTO mockCardDTO) {
        this.mockCardDTO = mockCardDTO;
    }
}
