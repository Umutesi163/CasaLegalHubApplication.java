package casa_legal_hub.controller;

import casa_legal_hub.dto.FinancialReportDTO;
import casa_legal_hub.dto.TaxReportDTO;
import casa_legal_hub.model.Payment;
import casa_legal_hub.model.PaymentStatus;
import casa_legal_hub.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class PaymentReportController {

    private final PaymentRepository paymentRepository;

    // Tax constants aligned with RRA
    private static final double VAT_RATE = 0.18;
    private static final double WHT_RATE = 0.15;
    private static final double CIT_RATE = 0.30;

    private static final String[] MONTH_NAMES = {
        "January","February","March","April","May","June",
        "July","August","September","October","November","December"
    };

    // ── Existing general report ──────────────────────────────────
    @GetMapping("/payments")
    public ResponseEntity<FinancialReportDTO> getFinancialReport() {
        List<Payment> payments = paymentRepository.findAll();

        double totalRevenue = payments.stream()
                .mapToDouble(p -> p.getAmount() != null ? p.getAmount() : 0).sum();

        Map<String, Double> byType = payments.stream()
                .filter(p -> p.getPaymentType() != null)
                .collect(Collectors.groupingBy(Payment::getPaymentType,
                        Collectors.summingDouble(p -> p.getAmount() != null ? p.getAmount() : 0)));

        Map<String, Double> byLawyer = payments.stream()
                .filter(p -> p.getLawyer() != null)
                .collect(Collectors.groupingBy(p -> p.getLawyer().getFullName(),
                        Collectors.summingDouble(p -> p.getAmount() != null ? p.getAmount() : 0)));

        return ResponseEntity.ok(new FinancialReportDTO(totalRevenue, (long) payments.size(), byType, byLawyer));
    }

    // ── RRA Tax Report ───────────────────────────────────────────
    @GetMapping("/tax")
    public ResponseEntity<TaxReportDTO> getTaxReport(@RequestParam(defaultValue = "0") int year) {
        int reportYear = year == 0 ? LocalDate.now().getYear() : year;

        // Only APPROVED payments count as taxable income
        List<Payment> approved = paymentRepository.findAll().stream()
                .filter(p -> p.getStatus() == PaymentStatus.APPROVED)
                .filter(p -> p.getPaymentDate() != null &&
                             p.getPaymentDate().getYear() == reportYear)
                .collect(Collectors.toList());

        // ── Annual totals ────────────────────────────────────────
        double grossIncome = approved.stream()
                .mapToDouble(p -> p.getAmount() != null ? p.getAmount() : 0).sum();
        double vatCollected       = grossIncome * VAT_RATE;
        double withholdingTax     = grossIncome * WHT_RATE;
        double netBeforeCIT       = grossIncome - vatCollected - withholdingTax;
        double corporateIncomeTax = Math.max(0, netBeforeCIT * CIT_RATE);
        double netAfterCIT        = netBeforeCIT - corporateIncomeTax;

        // ── Monthly breakdown ────────────────────────────────────
        Map<Integer, List<Payment>> byMonth = approved.stream()
                .collect(Collectors.groupingBy(p -> p.getPaymentDate().getMonthValue()));

        List<TaxReportDTO.MonthlyBreakdown> monthly = new ArrayList<>();
        for (int m = 1; m <= 12; m++) {
            List<Payment> monthPayments = byMonth.getOrDefault(m, Collections.emptyList());
            double gross = monthPayments.stream()
                    .mapToDouble(p -> p.getAmount() != null ? p.getAmount() : 0).sum();
            monthly.add(new TaxReportDTO.MonthlyBreakdown(
                    MONTH_NAMES[m - 1], m,
                    gross,
                    gross * VAT_RATE,
                    gross * WHT_RATE,
                    gross - (gross * VAT_RATE) - (gross * WHT_RATE),
                    (long) monthPayments.size()
            ));
        }

        // ── Quarterly summary ────────────────────────────────────
        Map<String, Double> quarterlyGross = new LinkedHashMap<>();
        Map<String, Double> quarterlyVAT   = new LinkedHashMap<>();
        String[] quarters = {"Q1 (Jan-Mar)", "Q2 (Apr-Jun)", "Q3 (Jul-Sep)", "Q4 (Oct-Dec)"};
        int[][] qMonths   = {{1,2,3},{4,5,6},{7,8,9},{10,11,12}};
        for (int q = 0; q < 4; q++) {
            final int[] months = qMonths[q];
            double qGross = approved.stream()
                    .filter(p -> {
                        int m = p.getPaymentDate().getMonthValue();
                        return m == months[0] || m == months[1] || m == months[2];
                    })
                    .mapToDouble(p -> p.getAmount() != null ? p.getAmount() : 0).sum();
            quarterlyGross.put(quarters[q], qGross);
            quarterlyVAT.put(quarters[q], qGross * VAT_RATE);
        }

        // ── By payment type ──────────────────────────────────────
        Map<String, Double> byType = approved.stream()
                .filter(p -> p.getPaymentType() != null)
                .collect(Collectors.groupingBy(Payment::getPaymentType,
                        Collectors.summingDouble(p -> p.getAmount() != null ? p.getAmount() : 0)));

        // ── By lawyer ────────────────────────────────────────────
        Map<String, Double> byLawyer = approved.stream()
                .filter(p -> p.getLawyer() != null)
                .collect(Collectors.groupingBy(p -> p.getLawyer().getFullName(),
                        Collectors.summingDouble(p -> p.getAmount() != null ? p.getAmount() : 0)));

        // ── RRA declaration deadlines ────────────────────────────
        int currentMonth = LocalDate.now().getMonthValue();
        String vatDeadline = String.format("15 %s %d (for %s income)",
                MONTH_NAMES[currentMonth % 12], currentMonth == 12 ? reportYear + 1 : reportYear,
                MONTH_NAMES[currentMonth - 1]);
        String citDeadline = String.format("31 March %d (for FY %d)", reportYear + 1, reportYear);

        TaxReportDTO report = new TaxReportDTO(
                reportYear,
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm")),
                grossIncome, vatCollected, withholdingTax,
                netBeforeCIT, corporateIncomeTax, netAfterCIT,
                monthly, quarterlyGross, quarterlyVAT,
                byType, byLawyer,
                vatDeadline, citDeadline
        );

        return ResponseEntity.ok(report);
    }
}
