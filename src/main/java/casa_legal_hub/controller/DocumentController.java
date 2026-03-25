package casa_legal_hub.controller;

import casa_legal_hub.dto.DocumentResponseDTO;
import casa_legal_hub.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocumentResponseDTO> upload(
            @RequestParam String title,
            @RequestParam String documentType,
            @RequestParam Long caseId,
            @RequestParam Long clientId,
            @RequestParam Long lawyerId,
            @RequestParam MultipartFile file) throws IOException {
        return ResponseEntity.ok(documentService.uploadDocument(title, documentType, file, caseId, clientId, lawyerId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(documentService.getDocumentById(id));
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> download(@PathVariable Long id) {
        DocumentResponseDTO doc = documentService.getDocumentById(id);
        byte[] data = documentService.downloadDocument(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + doc.getFileName() + "\"")
                .contentType(MediaType.parseMediaType(doc.getContentType()))
                .body(data);
    }

    @GetMapping
    public ResponseEntity<List<DocumentResponseDTO>> getAll() {
        return ResponseEntity.ok(documentService.getAllDocuments());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        documentService.deleteDocument(id);
        return ResponseEntity.ok("Deleted successfully");
    }

    @GetMapping("/case/{caseId}")
    public ResponseEntity<List<DocumentResponseDTO>> getByCase(@PathVariable Long caseId) {
        return ResponseEntity.ok(documentService.getDocumentsByCase(caseId));
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<DocumentResponseDTO>> getByClient(@PathVariable Long clientId) {
        return ResponseEntity.ok(documentService.getDocumentsByClient(clientId));
    }
}
