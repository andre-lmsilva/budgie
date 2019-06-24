package io.geekmind.budgie.fixture;

import io.geekmind.budgie.model.dto.BalanceSummary;

import java.math.BigDecimal;

public class BalanceSummaryFixture {

    public static BalanceSummary get() {
        return new BalanceSummary(
            BigDecimal.valueOf(150.00D),
            BigDecimal.valueOf(300.00D),
            BigDecimal.valueOf(-150.00D),
            BigDecimal.valueOf(270.00D)
        );
    }
}
