package lk.ijse.paymentservice.service;

import lk.ijse.paymentservice.dto.PaymentDTO;
import lk.ijse.paymentservice.response.ApiResponse;

public interface PaymentService {
    ApiResponse<?> makePayment(PaymentDTO dto);
}
