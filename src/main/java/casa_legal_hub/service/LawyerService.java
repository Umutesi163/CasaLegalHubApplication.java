package casa_legal_hub.service;
import casa_legal_hub.dto.LawyerRequestDTO;
import casa_legal_hub.dto.LawyerResponseDTO;

import java.util.List;

public interface LawyerService {

    LawyerResponseDTO createLawyer(LawyerRequestDTO dto);

    LawyerResponseDTO getLawyerById(Long id);

    List<LawyerResponseDTO> getAllLawyers();

    void deleteLawyer(Long id);
}