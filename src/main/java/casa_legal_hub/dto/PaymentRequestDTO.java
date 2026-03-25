package casa_legal_hub.dto;

import lombok.Data;

@Data
public class PaymentRequestDTO {
    private Double amount;
    private String paymentType;
    private String description;
    private Long caseId;
    private Long clientId;
    private Long lawyerId;
}