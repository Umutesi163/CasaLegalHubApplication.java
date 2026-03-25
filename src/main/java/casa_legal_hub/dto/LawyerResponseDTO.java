package casa_legal_hub.dto;
import lombok.Data;

@Data
public class LawyerResponseDTO {

    private Long id;
    private String fullName;
    private String specialization;
    private String email;
}