package casa_legal_hub.repository;

import casa_legal_hub.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByLawyerId(Long lawyerId);
    List<Payment> findByLegalCaseId(Long caseId);
}
