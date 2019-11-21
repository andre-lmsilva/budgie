package io.geekmind.budgie.fixture;

import io.geekmind.budgie.model.dto.BalanceDates;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BalanceDatesFixture {

    public static BalanceDates getBalanceDatesForCurrentPeriod() {
        return new BalanceDates(
            LocalDate.now(),
            LocalDate.now().withDayOfMonth(1),
            LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()),
            LocalDate.now().plusMonths(1L).plusDays(5L),
            LocalDate.now().minusMonths(1L),
            LocalDate.now().plusMonths(1L),
            30,
            BigDecimal.ZERO
        );
    }

}
