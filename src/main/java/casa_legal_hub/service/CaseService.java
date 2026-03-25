package casa_legal_hub.service;
import casa_legal_hub.dto.CaseRequestDTO;
import casa_legal_hub.dto.CaseResponseDTO;
import casa_legal_hub.model.CaseStatus;

import java.util.List;

public interface CaseService {

    CaseResponseDTO createCase(CaseRequestDTO dto);

    CaseResponseDTO getCaseById(Long id);

    List<CaseResponseDTO> getAllCases();

    void deleteCase(Long id);

    List<CaseResponseDTO> getCasesByStatus(CaseStatus status);

    List<CaseResponseDTO> getCasesByClientId(Long clientId);

    List<CaseResponseDTO> getCasesByLawyerId(Long lawyerId);
}