package lk.ijse.vehicleservice.response;

import lk.ijse.vehicleservice.dto.VehicleDTO;

public class VehicleResponse {
    private boolean success;
    private String message;
    private VehicleDTO data;

    public VehicleResponse(boolean success, String message, VehicleDTO data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public VehicleDTO getData() {
        return data;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(VehicleDTO data) {
        this.data = data;
    }
}
