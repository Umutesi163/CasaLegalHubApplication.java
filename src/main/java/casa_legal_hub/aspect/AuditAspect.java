package casa_legal_hub.aspect;

import casa_legal_hub.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditLogService auditLogService;

    @AfterReturning("execution(* casa_legal_hub.service.*ServiceImpl.create*(..))")
    public void logCreate(JoinPoint jp) {
        log("CREATE", jp);
    }

    @AfterReturning("execution(* casa_legal_hub.service.*ServiceImpl.delete*(..))")
    public void logDelete(JoinPoint jp) {
        log("DELETE", jp);
    }

    @AfterReturning("execution(* casa_legal_hub.service.*ServiceImpl.upload*(..))")
    public void logUpload(JoinPoint jp) {
        log("UPLOAD", jp);
    }

    private void log(String action, JoinPoint jp) {
        String username = "anonymous";
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            username = auth.getName();
        }
        String entity = jp.getTarget().getClass().getSimpleName().replace("ServiceImpl", "");
        String details = jp.getSignature().getName() + " called";
        auditLogService.log(username, action, entity, details);
    }
}
