package casa_legal_hub.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserFileResponseDTO {
    private Long id;
    private String title;
    private String description;
    private String category;
    private String tags;
    private String fileName;
    private String contentType;
    private LocalDateTime uploadedAt;
    private String ownerUsername;
    private Long originalSize;   // real file size shown to user
    private Long storedSize;     // compressed size in DB
    private String savedPercent; // e.g. "62% saved"
}
