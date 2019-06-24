package io.geekmind.budgie.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BalanceSummary {

    private BigDecimal finalBalance;

    private BigDecimal totalIncomes;

    private BigDecimal totalExpenses;

    private BigDecimal balanceUpToDate;

}
