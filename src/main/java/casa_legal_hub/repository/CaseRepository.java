package casa_legal_hub.repository;
import casa_legal_hub.model.LegalCase;
import casa_legal_hub.model.CaseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CaseRepository extends JpaRepository<LegalCase, Long> {

    List<LegalCase> findByStatus(CaseStatus status);

    List<LegalCase> findByLawyerId(Long lawyerId);
}