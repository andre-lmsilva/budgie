package io.geekmind.budgie.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryBalanceSummary {

    private ExistingCategory category;

    private BigDecimal balance;

    private BigDecimal expensesConsumptionPercentage;

}
