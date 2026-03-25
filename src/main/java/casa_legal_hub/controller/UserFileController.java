package casa_legal_hub.controller;

import casa_legal_hub.dto.UserFileResponseDTO;
import casa_legal_hub.exception.ResourceNotFoundException;
import casa_legal_hub.model.User;
import casa_legal_hub.model.UserFile;
import casa_legal_hub.repository.UserFileRepository;
import casa_legal_hub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@RestController
@RequestMapping("/api/my-files")
@RequiredArgsConstructor
public class UserFileController {

    private final UserFileRepository userFileRepository;
    private final UserRepository userRepository;

    // ── Upload (compress before storing) ────────────────────────
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Transactional
    public ResponseEntity<UserFileResponseDTO> upload(
            @RequestParam(required = false, defaultValue = "") String title,
            @RequestParam(required = false, defaultValue = "") String description,
            @RequestParam(required = false, defaultValue = "OTHER") String category,
            @RequestParam(required = false, defaultValue = "") String tags,
            @RequestParam MultipartFile file,
            Authentication auth) throws IOException {

        User owner = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        byte[] original   = file.getBytes();
        byte[] compressed = compress(original);

        UserFile uf = new UserFile();
        uf.setTitle(title.isBlank() ? file.getOriginalFilename() : title);
        uf.setDescription(description);
        uf.setCategory(category.toUpperCase());
        uf.setTags(tags.toLowerCase());
        uf.setFileName(file.getOriginalFilename());
        uf.setContentType(file.getContentType());
        uf.setFileData(compressed);
        uf.setOriginalSize((long) original.length);
        uf.setStoredSize((long) compressed.length);
        uf.setUploadedAt(LocalDateTime.now());
        uf.setOwner(owner);

        return ResponseEntity.ok(toDTO(userFileRepository.save(uf)));
    }

    // ── List (no file data loaded — metadata only) ───────────────
    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<List<UserFileResponseDTO>> getMyFiles(
            @RequestParam(required = false) String category,
            Authentication auth) {

        String username = auth.getName();
        List<UserFile> files = (category != null && !category.isBlank())
                ? userFileRepository.findByOwnerUsernameAndCategoryOrderByUploadedAtDesc(username, category.toUpperCase())
                : userFileRepository.findByOwnerUsernameOrderByUploadedAtDesc(username);

        return ResponseEntity.ok(files.stream().map(this::toDTO).toList());
    }

    // ── Download (decompress before sending) ────────────────────
    @GetMapping("/{id}/download")
    @Transactional(readOnly = true)
    public ResponseEntity<byte[]> download(@PathVariable Long id, Authentication auth) throws IOException {
        UserFile uf = userFileRepository.findByIdAndOwnerUsername(id, auth.getName())
                .orElseThrow(() -> new ResourceNotFoundException("File not found"));

        byte[] data = decompress(uf.getFileData());
        String ct   = (uf.getContentType() != null && !uf.getContentType().isBlank())
                ? uf.getContentType() : "application/octet-stream";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + uf.getFileName() + "\"")
                .contentType(MediaType.parseMediaType(ct))
                .body(data);
    }

    // ── Delete ───────────────────────────────────────────────────
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<String> delete(@PathVariable Long id, Authentication auth) {
        UserFile uf = userFileRepository.findByIdAndOwnerUsername(id, auth.getName())
                .orElseThrow(() -> new ResourceNotFoundException("File not found"));
        userFileRepository.delete(uf);
        return ResponseEntity.ok("Deleted");
    }

    // ── GZIP compress ────────────────────────────────────────────
    private byte[] compress(byte[] data) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (GZIPOutputStream gz = new GZIPOutputStream(bos)) {
            gz.write(data);
        }
        return bos.toByteArray();
    }

    // ── GZIP decompress ──────────────────────────────────────────
    private byte[] decompress(byte[] data) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (GZIPInputStream gz = new GZIPInputStream(new ByteArrayInputStream(data))) {
            byte[] buf = new byte[4096];
            int n;
            while ((n = gz.read(buf)) != -1) bos.write(buf, 0, n);
        }
        return bos.toByteArray();
    }

    // ── DTO (never expose fileData) ──────────────────────────────
    private UserFileResponseDTO toDTO(UserFile uf) {
        UserFileResponseDTO dto = new UserFileResponseDTO();
        dto.setId(uf.getId());
        dto.setTitle(uf.getTitle());
        dto.setDescription(uf.getDescription());
        dto.setCategory(uf.getCategory());
        dto.setTags(uf.getTags());
        dto.setFileName(uf.getFileName());
        dto.setContentType(uf.getContentType());
        dto.setUploadedAt(uf.getUploadedAt());
        dto.setOwnerUsername(uf.getOwner() != null ? uf.getOwner().getUsername() : "");
        dto.setOriginalSize(uf.getOriginalSize() != null ? uf.getOriginalSize() : 0L);
        dto.setStoredSize(uf.getStoredSize()   != null ? uf.getStoredSize()   : 0L);

        // Calculate saved %
        if (uf.getOriginalSize() != null && uf.getOriginalSize() > 0 && uf.getStoredSize() != null) {
            long saved = uf.getOriginalSize() - uf.getStoredSize();
            int pct = (int) (saved * 100 / uf.getOriginalSize());
            dto.setSavedPercent(pct > 0 ? pct + "% saved" : "");
        }
        return dto;
    }
}
