package casa_legal_hub.service;

import casa_legal_hub.dto.LawyerRequestDTO;
import casa_legal_hub.dto.LawyerResponseDTO;
import casa_legal_hub.exception.ResourceNotFoundException;
import casa_legal_hub.mapper.LawyerMapper;
import casa_legal_hub.model.Lawyer;
import casa_legal_hub.repository.LawyerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LawyerServiceImpl implements LawyerService {

    private final LawyerRepository lawyerRepository;
    private final LawyerMapper lawyerMapper;

    public LawyerServiceImpl(LawyerRepository lawyerRepository, LawyerMapper lawyerMapper) {
        this.lawyerRepository = lawyerRepository;
        this.lawyerMapper = lawyerMapper;
    }

    @Override
    public LawyerResponseDTO createLawyer(LawyerRequestDTO dto) {
        Lawyer lawyer = lawyerMapper.toEntity(dto);
        Lawyer saved = lawyerRepository.save(lawyer);
        return lawyerMapper.toDTO(saved);
    }

    @Override
    public LawyerResponseDTO getLawyerById(Long id) {

        Lawyer lawyer = lawyerRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Lawyer not found with id: " + id)
                );

        return lawyerMapper.toDTO(lawyer);
    }

    @Override
    public List<LawyerResponseDTO> getAllLawyers() {
        return lawyerRepository.findAll()
                .stream()
                .map(lawyerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteLawyer(Long id) {

        Lawyer lawyer = lawyerRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Lawyer not found with id: " + id)
                );

        lawyerRepository.delete(lawyer);
    }
}