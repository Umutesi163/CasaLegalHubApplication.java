package casa_legal_hub.mapper;
import casa_legal_hub.dto.CaseResponseDTO;
import casa_legal_hub.model.LegalCase;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CaseMapper {

    @Mapping(source = "lawyer.fullName", target = "lawyerName")
    @Mapping(source = "serviceType",     target = "serviceType")
    CaseResponseDTO toDTO(LegalCase legalCase);
}
