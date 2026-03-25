package casa_legal_hub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class FinancialReportDTO {
    private Double totalRevenue;
    private Long totalPayments;
    private Map<String, Double> revenueByPaymentType;
    private Map<String, Double> revenueByLawyer;
}
