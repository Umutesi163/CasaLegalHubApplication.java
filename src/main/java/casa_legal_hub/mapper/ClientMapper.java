package casa_legal_hub.mapper;
import casa_legal_hub.dto.ClientRequestDTO;
import casa_legal_hub.dto.ClientResponseDTO;
import casa_legal_hub.model.Client;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    Client toEntity(ClientRequestDTO dto);

    ClientResponseDTO toDTO(Client client);
}
