package casa_legal_hub.dto;
import casa_legal_hub.model.CaseStatus;
import casa_legal_hub.model.ServiceType;
import lombok.Data;

@Data
public class CaseRequestDTO {
    private String title;
    private String description;
    private CaseStatus status;
    private ServiceType serviceType;
    private Long lawyerId;
}
