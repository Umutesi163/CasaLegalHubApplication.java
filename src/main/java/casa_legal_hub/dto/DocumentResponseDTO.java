package casa_legal_hub.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DocumentResponseDTO {

    private Long id;
    private String title;
    private String documentType;
    private String fileName;
    private String contentType;
    private LocalDateTime uploadedAt;
    private Long caseId;
    private Long clientId;
    private Long lawyerId;
}
