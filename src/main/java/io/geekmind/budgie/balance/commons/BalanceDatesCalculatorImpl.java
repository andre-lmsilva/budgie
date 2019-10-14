package io.geekmind.budgie.balance.commons;

import io.geekmind.budgie.model.dto.ExistingAccount;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Provides the capability to calculate the dates related with the balance calculation.
 *
 * @author Andre Silva
 */
@Component
public class BalanceDatesCalculatorImpl implements BalanceDatesCalculator {

    @Override
    public LocalDate calculatePeriodEndDate(LocalDate referenceDate, ExistingAccount account) {
        LocalDate periodEndDate = referenceDate.plusMonths(1);
        if (referenceDate.getDayOfMonth() < account.getMonthStartingAt()) {
            periodEndDate = periodEndDate.minusMonths(1);
        }

        if (periodEndDate.lengthOfMonth() < account.getMonthStartingAt()) {
            periodEndDate = periodEndDate.withDayOfMonth(periodEndDate.lengthOfMonth());
        } else {
            periodEndDate = periodEndDate.withDayOfMonth(account.getMonthStartingAt());
        }
        return periodEndDate.minusDays(1);
    }

    @Override
    public LocalDate calculatePeriodStartDate(LocalDate periodEndDate) {
        return periodEndDate.minusMonths(1L).plusDays(1);
    }

    @Override
    public LocalDate calculatePeriodBillingDate(LocalDate periodEndDate, ExistingAccount account) {
        LocalDate billingDate = periodEndDate;
        int billingDay = account.getMonthBillingDayAt();

        if (billingDay < billingDate.getDayOfMonth()) {
            billingDate = billingDate.plusMonths(1);
        }

        if (billingDay > billingDate.lengthOfMonth()) {
            billingDay = billingDate.lengthOfMonth();
        }
        return billingDate.withDayOfMonth(billingDay);
    }

    @Override
    public Integer calculateDaysUntilEndOfPeriod(LocalDate periodEndDate) {
        if (periodEndDate.isBefore(LocalDate.now())) {
            return 0;
        }
        return ((Long) ChronoUnit.DAYS.between(LocalDate.now(), periodEndDate)).intValue();
    }

    @Override
    public LocalDate calculatePeriodEndDateBasedOnBillingDate(LocalDate billingDate, ExistingAccount account) {
        LocalDate periodEndDate = billingDate.minusMonths(1L);
        periodEndDate = periodEndDate.withDayOfMonth(periodEndDate.lengthOfMonth());
        if (periodEndDate.lengthOfMonth() >= account.getMonthStartingAt()) {
            periodEndDate = periodEndDate.withDayOfMonth(account.getMonthStartingAt());
        }
        return periodEndDate.minusDays(1);
    }
}
