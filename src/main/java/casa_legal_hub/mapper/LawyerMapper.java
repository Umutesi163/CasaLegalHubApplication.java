package casa_legal_hub.mapper;
import casa_legal_hub.dto.LawyerRequestDTO;
import casa_legal_hub.dto.LawyerResponseDTO;
import casa_legal_hub.model.Lawyer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LawyerMapper {

    Lawyer toEntity(LawyerRequestDTO dto);

    LawyerResponseDTO toDTO(Lawyer lawyer);
}