package casa_legal_hub.service;

import casa_legal_hub.model.AuditLog;
import casa_legal_hub.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public void log(String performedBy, String action, String entity, String details) {
        AuditLog log = new AuditLog(null, performedBy, action, entity, details, LocalDateTime.now());
        auditLogRepository.save(log);
    }

    public List<AuditLog> getAllLogs() {
        return auditLogRepository.findAll();
    }

    public List<AuditLog> getLogsByUser(String username) {
        return auditLogRepository.findByPerformedByOrderByTimestampDesc(username);
    }
}
