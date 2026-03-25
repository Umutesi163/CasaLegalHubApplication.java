package casa_legal_hub.dto;
import casa_legal_hub.model.Role;
import lombok.Data;

@Data
public class UserResponseDTO {

    private Long id;
    private String username;
    private String email;
    private Role role;
}
