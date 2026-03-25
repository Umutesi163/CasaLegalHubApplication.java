package casa_legal_hub.service;

import casa_legal_hub.dto.CaseRequestDTO;
import casa_legal_hub.dto.CaseResponseDTO;
import casa_legal_hub.exception.ResourceNotFoundException;
import casa_legal_hub.mapper.CaseMapper;
import casa_legal_hub.model.LegalCase;
import casa_legal_hub.model.CaseStatus;
import casa_legal_hub.model.Lawyer;
import casa_legal_hub.repository.CaseRepository;
import casa_legal_hub.repository.LawyerRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CaseServiceImpl implements CaseService {

    private final CaseRepository caseRepository;
    private final LawyerRepository lawyerRepository;
    private final CaseMapper caseMapper;

    public CaseServiceImpl(CaseRepository caseRepository, LawyerRepository lawyerRepository, CaseMapper caseMapper) {
        this.caseRepository = caseRepository;
        this.lawyerRepository = lawyerRepository;
        this.caseMapper = caseMapper;
    }

    @Override
    public CaseResponseDTO createCase(CaseRequestDTO dto) {
        Lawyer lawyer = lawyerRepository.findById(dto.getLawyerId())
                .orElseThrow(() -> new ResourceNotFoundException("Lawyer not found with id: " + dto.getLawyerId()));

        LegalCase legalCase = new LegalCase();
        legalCase.setTitle(dto.getTitle());
        legalCase.setDescription(dto.getDescription());
        legalCase.setStatus(dto.getStatus());
        legalCase.setServiceType(dto.getServiceType());
        legalCase.setCreatedDate(LocalDate.now());
        legalCase.setLawyer(lawyer);

        return caseMapper.toDTO(caseRepository.save(legalCase));
    }

    @Override
    public CaseResponseDTO getCaseById(Long id) {
        return caseMapper.toDTO(caseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Case not found with id: " + id)));
    }

    @Override
    public List<CaseResponseDTO> getAllCases() {
        return caseRepository.findAll().stream().map(caseMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public void deleteCase(Long id) {
        caseRepository.delete(caseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Case not found with id: " + id)));
    }

    @Override
    public List<CaseResponseDTO> getCasesByStatus(CaseStatus status) {
        return caseRepository.findByStatus(status).stream().map(caseMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<CaseResponseDTO> getCasesByLawyerId(Long lawyerId) {
        return caseRepository.findByLawyerId(lawyerId).stream().map(caseMapper::toDTO).collect(Collectors.toList());
    }
}
