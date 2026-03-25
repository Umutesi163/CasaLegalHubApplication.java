package casa_legal_hub.controller;
import casa_legal_hub.dto.LawyerRequestDTO;
import casa_legal_hub.dto.LawyerResponseDTO;
import casa_legal_hub.service.LawyerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lawyers")
public class LawyerController {

    private final LawyerService lawyerService;

    public LawyerController(LawyerService lawyerService) {
        this.lawyerService = lawyerService;
    }

    @PostMapping
    public ResponseEntity<LawyerResponseDTO> createLawyer(@RequestBody LawyerRequestDTO dto) {
        return ResponseEntity.ok(lawyerService.createLawyer(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LawyerResponseDTO> getLawyerById(@PathVariable Long id) {
        return ResponseEntity.ok(lawyerService.getLawyerById(id));
    }

    @GetMapping
    public ResponseEntity<List<LawyerResponseDTO>> getAllLawyers() {
        return ResponseEntity.ok(lawyerService.getAllLawyers());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLawyer(@PathVariable Long id) {
        lawyerService.deleteLawyer(id);
        return ResponseEntity.ok("Lawyer deleted successfully");
    }
}