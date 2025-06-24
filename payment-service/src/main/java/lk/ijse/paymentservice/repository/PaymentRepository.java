package lk.ijse.paymentservice.repository;

import lk.ijse.paymentservice.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByEmail(String email);
    List<Payment> findByVehicleNumber(String vehicleNumber);
    List<Payment> findByEmailAndVehicleNumber(String email, String vehicleNumber);
}
