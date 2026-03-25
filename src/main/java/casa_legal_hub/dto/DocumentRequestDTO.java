package casa_legal_hub.dto;
import lombok.Data;

@Data
public class DocumentRequestDTO {
    private String title;
    private String documentType;
    private Long caseId;
    private Long lawyerId;
}
