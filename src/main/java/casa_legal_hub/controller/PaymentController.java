package casa_legal_hub.controller;

import casa_legal_hub.dto.PaymentRequestDTO;
import casa_legal_hub.dto.PaymentResponseDTO;
import casa_legal_hub.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<PaymentResponseDTO> createPayment(@RequestBody PaymentRequestDTO dto) {
        return ResponseEntity.ok(paymentService.createPayment(dto));
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponseDTO>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    @GetMapping("/case/{caseId}")
    public ResponseEntity<List<PaymentResponseDTO>> getByCase(@PathVariable Long caseId) {
        return ResponseEntity.ok(paymentService.getPaymentsByCase(caseId));
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<PaymentResponseDTO> approve(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.approvePayment(id));
    }

    @PutMapping("/{id}/refund")
    public ResponseEntity<PaymentResponseDTO> refund(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.refundPayment(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        paymentService.deletePayment(id);
        return ResponseEntity.ok("Payment deleted successfully");
    }
}
