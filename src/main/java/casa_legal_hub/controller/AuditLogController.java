package casa_legal_hub.controller;

import casa_legal_hub.model.AuditLog;
import casa_legal_hub.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audit-logs")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogService auditLogService;

    @GetMapping
    public ResponseEntity<List<AuditLog>> getAll() {
        return ResponseEntity.ok(auditLogService.getAllLogs());
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<List<AuditLog>> getByUser(@PathVariable String username) {
        return ResponseEntity.ok(auditLogService.getLogsByUser(username));
    }
}
