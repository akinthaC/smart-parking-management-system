package lk.ijse.paymentservice.service.impl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.draw.LineSeparator;
import lk.ijse.paymentservice.dto.MockCardDTO;
import lk.ijse.paymentservice.dto.ParkingLogDTO;
import lk.ijse.paymentservice.dto.PaymentDTO;
import lk.ijse.paymentservice.entity.Payment;
import lk.ijse.paymentservice.response.ApiResponse;
import lk.ijse.paymentservice.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import lk.ijse.paymentservice.repository.PaymentRepository;

import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private RestTemplate restTemplate;



    @Override
    public ApiResponse<?> makePayment(PaymentDTO dto) {
        String PARKING_LOG_SERVICE_URL = "http://localhost:8084/parking-service/api/v1/parking-spaces/last-completed?email=%s&vehicleNumber=%s";
        String markPaidLogUrl = String.format(
                "http://localhost:8084/parking-service/api/v1/parking-spaces/mark-paid?email=%s&vehicleNumber=%s",
                dto.getEmail(),
                dto.getVehicleNumber()
        );
        String parkingLogUrl = String.format(PARKING_LOG_SERVICE_URL, dto.getEmail(), dto.getVehicleNumber());
        try {
            ResponseEntity<ParkingLogDTO> response = restTemplate.getForEntity(parkingLogUrl, ParkingLogDTO.class);
            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                return new ApiResponse<>("❌ No completed parking log found for the given email/vehicle", null);
            }

            ParkingLogDTO parkingLog = response.getBody();
            System.out.println(parkingLog.isPaid());

            if (Boolean.TRUE.equals(parkingLog.isPaid())) {
                return new ApiResponse<>("This vehicle already paid the payment, total payment is " + parkingLog.getTotalFee(), null);
            }


            if ("CARD".equalsIgnoreCase(dto.getMethod())) {
                String validationResult = validateCard(dto.getMockCardDTO());
                if (validationResult != null) {
                    return new ApiResponse<>(validationResult, null);
                }
            } else if ("CASH".equalsIgnoreCase(dto.getMethod())) {
                // No card details needed for CASH payment
            } else {
                return new ApiResponse<>("⚠️ Unsupported payment method: " + dto.getMethod(), null);
            }

            Payment payment = new Payment();
            payment.setParkingLog(parkingLog.getId());
            payment.setEmail(parkingLog.getEmail());
            payment.setVehicleNumber(parkingLog.getVehicleNumber());
            payment.setMethod(dto.getMethod());
            payment.setStatus("PAID");
            payment.setPaymentTime(LocalDateTime.now());
            payment.setDurationInMinutes(parkingLog.getDurationInMinutes());
            payment.setTotalFee(parkingLog.getTotalFee());


            paymentRepository.save(payment);
            HttpEntity<Void> requestEntity = new HttpEntity<>(new HttpHeaders());
            ResponseEntity<String> responsem = restTemplate.exchange(markPaidLogUrl, HttpMethod.PATCH, requestEntity, String.class);

            generateReceiptPdf(payment);



            PaymentDTO responseDTO = new PaymentDTO();
            responseDTO.setEmail(payment.getEmail());
            responseDTO.setVehicleNumber(payment.getVehicleNumber());
            responseDTO.setMethod(payment.getMethod());
            responseDTO.setStatus(payment.getStatus());
            responseDTO.setDurationInMinutes(payment.getDurationInMinutes());
            responseDTO.setTotalFee(payment.getTotalFee());

            return new ApiResponse<>("✅ Payment successful!", responseDTO);

        } catch (HttpClientErrorException.NotFound ex) {
            // log.error("Parking log not found", ex);
            return new ApiResponse<>("❌ No completed parking log found for the given email/vehicle", null);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ApiResponse<>("❌ Payment processing failed", null);
        }
    }

    private String validateCard(MockCardDTO card) {
        if (card == null
                || card.getCardNumber() == null
                || card.getCardHolder() == null
                || card.getCvv() == null
                || card.getExpiry() == null) {
            return "⚠️ You selected CARD payment. Please provide complete card details.";
        }
        if (!card.getCardNumber().matches("\\d{16}")) {
            return "⚠️ Invalid card number format. Must be 16 digits.";
        }
        // You can add more validations here (expiry date format, cvv length, etc.)
        return null;
    }


    private void generateReceiptPdf(Payment payment) {
        Document document = new Document();
        try {
            String folderPath = "receipts";
            File folder = new File(folderPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            String fileName = String.format("receipt_%s_%d.pdf", payment.getVehicleNumber(), System.currentTimeMillis());
            String filePath = folderPath + File.separator + fileName;
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Fonts
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
            Font labelFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font valueFont = new Font(Font.FontFamily.HELVETICA, 12);

            // 🧾 Title
            Paragraph title = new Paragraph("🧾 Smart Parking - Payment Receipt", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20f);
            document.add(title);

            // Divider
            LineSeparator ls = new LineSeparator();
            document.add(new Chunk(ls));

            // Receipt Details Table
            PdfPTable table = new PdfPTable(2); // 2 columns
            table.setWidthPercentage(100);
            table.setSpacingBefore(20f);
            table.setSpacingAfter(20f);
            table.setWidths(new float[]{2f, 5f});

            addRow(table, "Receipt ID:", UUID.randomUUID().toString(), labelFont, valueFont);
            addRow(table, "Email:", payment.getEmail(), labelFont, valueFont);
            addRow(table, "Vehicle Number:", payment.getVehicleNumber(), labelFont, valueFont);
            addRow(table, "Payment Method:", payment.getMethod(), labelFont, valueFont);
            addRow(table, "Total Fee (Rs.):", String.format("%.2f", payment.getTotalFee()), labelFont, valueFont);
            addRow(table, "Duration (min):", String.valueOf(payment.getDurationInMinutes()), labelFont, valueFont);
            addRow(table, "Payment Time:", payment.getPaymentTime().toString(), labelFont, valueFont);

            document.add(table);

            // Footer
            Paragraph footer = new Paragraph("Thank you for using Smart Parking!", headerFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            footer.setSpacingBefore(30f);
            document.add(footer);

            document.close();
            System.out.println("✅ Receipt PDF generated: " + filePath);

        } catch (Exception e) {
            System.err.println("❌ Error generating PDF: " + e.getMessage());
        }
    }

    private void addRow(PdfPTable table, String label, String value, Font labelFont, Font valueFont) {
        PdfPCell cell1 = new PdfPCell(new Phrase(label, labelFont));
        PdfPCell cell2 = new PdfPCell(new Phrase(value, valueFont));
        cell1.setBorder(Rectangle.NO_BORDER);
        cell2.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell1);
        table.addCell(cell2);
    }






}
