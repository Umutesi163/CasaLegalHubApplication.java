package casa_legal_hub.mapper;
import casa_legal_hub.dto.UserRequestDTO;
import casa_legal_hub.dto.UserResponseDTO;
import casa_legal_hub.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserRequestDTO dto);

    UserResponseDTO toDTO(User user);
}
