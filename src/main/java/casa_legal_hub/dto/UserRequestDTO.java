package casa_legal_hub.dto;
import casa_legal_hub.model.Role;
import lombok.Data;

@Data
public class UserRequestDTO {

    private String username;
    private String email;
    private String password;
    private Role role;
}
