package casa_legal_hub.model;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lawyers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Lawyer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    private String specialization;

    private String email;

    private String phone;

    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;
}
