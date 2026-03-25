package casa_legal_hub.dto;
import casa_legal_hub.model.CaseStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CaseResponseDTO {
    private Long id;
    private String title;
    private String description;
    private CaseStatus status;
    private String serviceType;
    private LocalDate createdDate;
    private String lawyerName;
}
