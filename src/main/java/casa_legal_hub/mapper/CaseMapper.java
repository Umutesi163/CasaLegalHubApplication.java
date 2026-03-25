package casa_legal_hub.mapper;
import casa_legal_hub.dto.CaseResponseDTO;
import casa_legal_hub.model.LegalCase;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CaseMapper {

    @Mapping(source = "client.fullName", target = "clientName")
    @Mapping(source = "lawyer.fullName", target = "lawyerName")
    CaseResponseDTO toDTO(LegalCase legalCase);
}