package io.geekmind.budgie.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BalanceSummary {

    private BigDecimal finalBalance = BigDecimal.ZERO;

    private BigDecimal totalIncomes = BigDecimal.ZERO;

    private BigDecimal totalExpenses = BigDecimal.ZERO;

    private BigDecimal balanceUpToDate = BigDecimal.ZERO;

}
