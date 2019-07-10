package io.geekmind.budgie.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BalanceDates {

    private LocalDate referenceDate;

    private LocalDate periodStartDate;

    private LocalDate periodEndDate;

    private LocalDate periodBillingDate;

    private LocalDate previousPeriodStartDate;

    private LocalDate nextPeriodStartDate;

    private Integer periodRemainingDays;
}
