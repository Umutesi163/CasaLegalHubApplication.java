package casa_legal_hub.mapper;

import casa_legal_hub.dto.DocumentResponseDTO;
import casa_legal_hub.model.Client;
import casa_legal_hub.model.Document;
import casa_legal_hub.model.LegalCase;
import casa_legal_hub.model.Lawyer;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

public class DocumentMapper {

    private DocumentMapper() {}

    public static Document toEntity(String title, String documentType,
                                    MultipartFile file, LegalCase legalCase,
                                    Client client, Lawyer lawyer) throws IOException {
        Document document = new Document();
        document.setTitle(title);
        document.setDocumentType(documentType);
        document.setFileName(file.getOriginalFilename());
        document.setContentType(file.getContentType());
        document.setFileData(file.getBytes());
        document.setUploadedAt(LocalDateTime.now());
        document.setLegalCase(legalCase);
        document.setClient(client);
        document.setLawyer(lawyer);
        return document;
    }

    public static DocumentResponseDTO toDTO(Document document) {
        DocumentResponseDTO dto = new DocumentResponseDTO();
        dto.setId(document.getId());
        dto.setTitle(document.getTitle());
        dto.setDocumentType(document.getDocumentType());
        dto.setFileName(document.getFileName());
        dto.setContentType(document.getContentType());
        dto.setUploadedAt(document.getUploadedAt());

        if (document.getLegalCase() != null)
            dto.setCaseId(document.getLegalCase().getId());
        if (document.getClient() != null)
            dto.setClientId(document.getClient().getId());
        if (document.getLawyer() != null)
            dto.setLawyerId(document.getLawyer().getId());

        return dto;
    }
}
