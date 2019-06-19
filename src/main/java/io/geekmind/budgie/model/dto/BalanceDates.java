package io.geekmind.budgie.model.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BalanceDates {

    private LocalDate referenceDate;

    private LocalDate periodStartDate;

    private LocalDate periodEndDate;

    private LocalDate periodBillingDate;

    private LocalDate previousPeriodStartDate;

    private LocalDate nextPeriodStartDate;
}
