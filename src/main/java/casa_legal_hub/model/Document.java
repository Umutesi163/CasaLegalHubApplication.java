package casa_legal_hub.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "documents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String documentType;

    private String fileName;

    private String contentType;

    @Lob
    @Column(columnDefinition = "BYTEA")
    private byte[] fileData;

    private LocalDateTime uploadedAt;

    @ManyToOne
    @JoinColumn(name = "case_id")
    private LegalCase legalCase;

    @ManyToOne
    @JoinColumn(name = "lawyer_id")
    private Lawyer lawyer;
}
