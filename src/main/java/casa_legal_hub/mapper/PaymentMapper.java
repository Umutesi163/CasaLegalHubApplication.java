package casa_legal_hub.mapper;

import casa_legal_hub.dto.PaymentResponseDTO;
import casa_legal_hub.model.Payment;

public class PaymentMapper {

    private PaymentMapper() {}

    public static PaymentResponseDTO toDTO(Payment p) {
        PaymentResponseDTO dto = new PaymentResponseDTO();
        dto.setId(p.getId());
        dto.setAmount(p.getAmount());
        dto.setPaymentType(p.getPaymentType());
        dto.setDescription(p.getDescription());
        dto.setPaymentDate(p.getPaymentDate());
        dto.setStatus(p.getStatus() != null ? p.getStatus().name() : "PENDING");
        dto.setLawyerName(p.getLawyer() != null ? p.getLawyer().getFullName() : null);
        dto.setCaseName(p.getLegalCase() != null ? p.getLegalCase().getTitle() : null);
        return dto;
    }
}
