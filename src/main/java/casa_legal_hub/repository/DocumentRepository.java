package casa_legal_hub.repository;

import casa_legal_hub.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    List<Document> findByLegalCaseId(Long caseId);

    List<Document> findByClientId(Long clientId);

    List<Document> findByLawyerId(Long lawyerId);
}
