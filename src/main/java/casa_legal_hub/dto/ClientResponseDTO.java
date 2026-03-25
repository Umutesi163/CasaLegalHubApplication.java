package casa_legal_hub.dto;
import lombok.Data;

@Data
public class ClientResponseDTO {

    private Long id;
    private String fullName;
    private String email;
    private String phone;
}
