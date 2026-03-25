package casa_legal_hub.dto;
import casa_legal_hub.model.ServiceType;
import lombok.Data;

@Data
public class LawyerRequestDTO {
    private String fullName;
    private String specialization;
    private String email;
    private String phone;
    private ServiceType serviceType;
}
