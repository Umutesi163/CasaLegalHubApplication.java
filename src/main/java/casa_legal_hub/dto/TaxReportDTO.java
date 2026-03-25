package casa_legal_hub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaxReportDTO {

    // Period info
    private int year;
    private String generatedAt;

    // Annual totals (APPROVED payments only)
    private Double grossIncome;
    private Double vatCollected;       // 18% of gross
    private Double withholdingTax;     // 15% of gross
    private Double netIncomeBeforeCIT; // gross - vat - wht
    private Double corporateIncomeTax; // 30% of net
    private Double netIncomeAfterCIT;

    // Monthly breakdown
    private List<MonthlyBreakdown> monthlyBreakdowns;

    // Quarterly summary
    private Map<String, Double> quarterlyGross;
    private Map<String, Double> quarterlyVAT;

    // By payment type
    private Map<String, Double> incomeByPaymentType;

    // By lawyer
    private Map<String, Double> incomeByLawyer;

    // Declaration deadlines
    private String vatDeadline;
    private String citDeadline;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MonthlyBreakdown {
        private String month;
        private int monthNumber;
        private Double grossIncome;
        private Double vatAmount;
        private Double whtAmount;
        private Double netIncome;
        private Long transactionCount;
    }
}
