package io.geekmind.budgie.model.dto.balance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Holds the calculated dates of a balance.
 *
 * @author Andre Silva
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BalanceDates {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate referenceDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate periodStartDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate periodEndDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate periodBillingDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate previousPeriodStartDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate nextPeriodStartDate;

    private Integer periodRemainingDays;

    private BigDecimal periodCompletion;

}
