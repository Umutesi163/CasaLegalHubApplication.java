package casa_legal_hub.dto;
import casa_legal_hub.model.CaseStatus;
import lombok.Data;

@Data
public class CaseRequestDTO {

    private String title;
    private String description;
    private CaseStatus status;

    private Long clientId;
    private Long lawyerId;
}
