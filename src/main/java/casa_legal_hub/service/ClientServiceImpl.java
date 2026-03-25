package casa_legal_hub.service;

import casa_legal_hub.dto.ClientRequestDTO;
import casa_legal_hub.dto.ClientResponseDTO;
import casa_legal_hub.exception.ResourceNotFoundException;
import casa_legal_hub.mapper.ClientMapper;
import casa_legal_hub.model.Client;
import casa_legal_hub.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    public ClientServiceImpl(ClientRepository clientRepository, ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
    }

    @Override
    public ClientResponseDTO createClient(ClientRequestDTO dto) {
        Client client = clientMapper.toEntity(dto);
        Client saved = clientRepository.save(client);
        return clientMapper.toDTO(saved);
    }

    @Override
    public ClientResponseDTO getClientById(Long id) {

        Client client = clientRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Client not found with id: " + id)
                );

        return clientMapper.toDTO(client);
    }

    @Override
    public List<ClientResponseDTO> getAllClients() {
        return clientRepository.findAll()
                .stream()
                .map(clientMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteClient(Long id) {

        Client client = clientRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Client not found with id: " + id)
                );

        clientRepository.delete(client);
    }
}