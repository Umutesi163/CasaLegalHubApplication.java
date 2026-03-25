package casa_legal_hub.service;

import casa_legal_hub.dto.PaymentRequestDTO;
import casa_legal_hub.dto.PaymentResponseDTO;

import java.util.List;

public interface PaymentService {
    PaymentResponseDTO createPayment(PaymentRequestDTO dto);
    List<PaymentResponseDTO> getAllPayments();
    List<PaymentResponseDTO> getPaymentsByCase(Long caseId);
    PaymentResponseDTO approvePayment(Long id);
    PaymentResponseDTO refundPayment(Long id);
    void deletePayment(Long id);
}
