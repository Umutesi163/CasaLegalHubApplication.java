package casa_legal_hub.service;

import casa_legal_hub.dto.DocumentResponseDTO;
import casa_legal_hub.exception.ResourceNotFoundException;
import casa_legal_hub.mapper.DocumentMapper;
import casa_legal_hub.model.*;
import casa_legal_hub.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final CaseRepository caseRepository;
    private final LawyerRepository lawyerRepository;

    @Override
    public DocumentResponseDTO uploadDocument(String title, String documentType,
                                              MultipartFile file, Long caseId,
                                              Long lawyerId) throws IOException {
        LegalCase legalCase = caseRepository.findById(caseId)
                .orElseThrow(() -> new ResourceNotFoundException("Case not found"));
        Lawyer lawyer = lawyerRepository.findById(lawyerId)
                .orElseThrow(() -> new ResourceNotFoundException("Lawyer not found"));

        return DocumentMapper.toDTO(documentRepository.save(
                DocumentMapper.toEntity(title, documentType, file, legalCase, lawyer)));
    }

    @Override
    public DocumentResponseDTO getDocumentById(Long id) {
        return DocumentMapper.toDTO(documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found with id: " + id)));
    }

    @Override
    public byte[] downloadDocument(Long id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found with id: " + id))
                .getFileData();
    }

    @Override
    public List<DocumentResponseDTO> getAllDocuments() {
        return documentRepository.findAll().stream().map(DocumentMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public void deleteDocument(Long id) {
        if (!documentRepository.existsById(id))
            throw new ResourceNotFoundException("Document not found with id: " + id);
        documentRepository.deleteById(id);
    }

    @Override
    public List<DocumentResponseDTO> getDocumentsByCase(Long caseId) {
        return documentRepository.findByLegalCaseId(caseId).stream()
                .map(DocumentMapper::toDTO).collect(Collectors.toList());
    }
}
