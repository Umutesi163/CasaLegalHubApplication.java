package casa_legal_hub.dto;


import lombok.Data;

@Data
public class DocumentRequestDTO {

    private String title;

    private String documentType;

    private String filePath;

    private Long caseId;

    private Long clientId;

    private Long lawyerId;
}