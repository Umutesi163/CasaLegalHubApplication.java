package casa_legal_hub.service;

import casa_legal_hub.dto.DocumentResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface DocumentService {

    DocumentResponseDTO uploadDocument(String title, String documentType,
                                       MultipartFile file, Long caseId,
                                       Long clientId, Long lawyerId) throws IOException;

    DocumentResponseDTO getDocumentById(Long id);

    byte[] downloadDocument(Long id);

    List<DocumentResponseDTO> getAllDocuments();

    void deleteDocument(Long id);

    List<DocumentResponseDTO> getDocumentsByCase(Long caseId);

    List<DocumentResponseDTO> getDocumentsByClient(Long clientId);
}
