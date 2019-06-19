package io.geekmind.budgie.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BalanceSummary {

    private BigDecimal finalBalance;

    private BigDecimal totalIncomes;

    private BigDecimal totalExpenses;

    private BigDecimal balanceUpToDate;

}
