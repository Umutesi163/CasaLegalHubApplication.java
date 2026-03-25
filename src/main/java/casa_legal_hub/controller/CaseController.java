package casa_legal_hub.controller;

import casa_legal_hub.dto.CaseRequestDTO;
import casa_legal_hub.dto.CaseResponseDTO;
import casa_legal_hub.model.CaseStatus;
import casa_legal_hub.service.CaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cases")
public class CaseController {

    private final CaseService caseService;

    public CaseController(CaseService caseService) {
        this.caseService = caseService;
    }

    @PostMapping
    public ResponseEntity<CaseResponseDTO> createCase(@RequestBody CaseRequestDTO dto) {
        return ResponseEntity.ok(caseService.createCase(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CaseResponseDTO> getCaseById(@PathVariable Long id) {
        return ResponseEntity.ok(caseService.getCaseById(id));
    }

    @GetMapping
    public ResponseEntity<List<CaseResponseDTO>> getAllCases() {
        return ResponseEntity.ok(caseService.getAllCases());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCase(@PathVariable Long id) {
        caseService.deleteCase(id);
        return ResponseEntity.ok("Case deleted successfully");
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<CaseResponseDTO>> getByStatus(@PathVariable CaseStatus status) {
        return ResponseEntity.ok(caseService.getCasesByStatus(status));
    }

    @GetMapping("/lawyer/{lawyerId}")
    public ResponseEntity<List<CaseResponseDTO>> getByLawyer(@PathVariable Long lawyerId) {
        return ResponseEntity.ok(caseService.getCasesByLawyerId(lawyerId));
    }
}