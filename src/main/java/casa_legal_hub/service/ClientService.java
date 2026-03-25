package casa_legal_hub.service;
import casa_legal_hub.dto.ClientRequestDTO;
import casa_legal_hub.dto.ClientResponseDTO;

import java.util.List;

public interface ClientService {

    ClientResponseDTO createClient(ClientRequestDTO dto);

    ClientResponseDTO getClientById(Long id);

    List<ClientResponseDTO> getAllClients();

    void deleteClient(Long id);
}