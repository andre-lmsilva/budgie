package io.geekmind.budgie.model.dto;

import io.geekmind.budgie.model.dto.category.ExistingCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryBalanceSummary {

    private ExistingCategory category;

    private BigDecimal balance;

    private BigDecimal expensesConsumptionPercentage;

    private BigDecimal maxExpensesConsumption;

    private BigDecimal maxExpenses;

    private Map<String, BigDecimal> balanceBreakDown;

    private List<ExistingRecord> records;

}
