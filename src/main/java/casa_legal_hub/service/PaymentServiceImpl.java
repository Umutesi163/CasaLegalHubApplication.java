package casa_legal_hub.service;

import casa_legal_hub.dto.PaymentRequestDTO;
import casa_legal_hub.dto.PaymentResponseDTO;
import casa_legal_hub.exception.ResourceNotFoundException;
import casa_legal_hub.mapper.PaymentMapper;
import casa_legal_hub.model.Payment;
import casa_legal_hub.model.PaymentStatus;
import casa_legal_hub.repository.CaseRepository;
import casa_legal_hub.repository.LawyerRepository;
import casa_legal_hub.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final CaseRepository caseRepository;
    private final LawyerRepository lawyerRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository,
                              CaseRepository caseRepository,
                              LawyerRepository lawyerRepository) {
        this.paymentRepository = paymentRepository;
        this.caseRepository    = caseRepository;
        this.lawyerRepository  = lawyerRepository;
    }

    @Override
    public PaymentResponseDTO createPayment(PaymentRequestDTO dto) {
        Payment payment = new Payment();
        payment.setAmount(dto.getAmount());
        payment.setPaymentType(dto.getPaymentType());
        payment.setDescription(dto.getDescription());
        payment.setPaymentDate(LocalDateTime.now());
        payment.setStatus(PaymentStatus.PENDING);

        payment.setLegalCase(caseRepository.findById(dto.getCaseId())
                .orElseThrow(() -> new ResourceNotFoundException("Case not found")));
        payment.setLawyer(lawyerRepository.findById(dto.getLawyerId())
                .orElseThrow(() -> new ResourceNotFoundException("Lawyer not found")));

        return PaymentMapper.toDTO(paymentRepository.save(payment));
    }

    @Override
    public List<PaymentResponseDTO> getAllPayments() {
        return paymentRepository.findAll().stream().map(PaymentMapper::toDTO).toList();
    }

    @Override
    public List<PaymentResponseDTO> getPaymentsByCase(Long caseId) {
        return paymentRepository.findByLegalCaseId(caseId).stream().map(PaymentMapper::toDTO).toList();
    }

    @Override
    public PaymentResponseDTO approvePayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));
        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new IllegalStateException("Only PENDING payments can be approved");
        }
        payment.setStatus(PaymentStatus.APPROVED);
        return PaymentMapper.toDTO(paymentRepository.save(payment));
    }

    @Override
    public PaymentResponseDTO refundPayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));
        if (payment.getStatus() != PaymentStatus.APPROVED) {
            throw new IllegalStateException("Only APPROVED payments can be refunded");
        }
        payment.setStatus(PaymentStatus.REFUNDED);
        return PaymentMapper.toDTO(paymentRepository.save(payment));
    }

    @Override
    public void deletePayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));
        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new IllegalStateException("Only PENDING payments can be deleted");
        }
        paymentRepository.deleteById(id);
    }
}
