package casa_legal_hub.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_files")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UserFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 1000)
    private String description;

    private String category;

    private String tags;

    private String fileName;

    private String contentType;

    /** Stored compressed (GZIP). Decompress on download. */
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "file_data")
    private byte[] fileData;

    /** Original file size in bytes (before compression) */
    private Long originalSize;

    /** Compressed size in bytes (what is actually stored) */
    private Long storedSize;

    private LocalDateTime uploadedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;
}
