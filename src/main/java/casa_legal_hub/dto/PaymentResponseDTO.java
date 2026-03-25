package casa_legal_hub.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PaymentResponseDTO {
    private Long id;
    private Double amount;
    private String paymentType;
    private String description;
    private LocalDateTime paymentDate;
    private String status;
    private String clientName;
    private String lawyerName;
    private String caseName;
}
