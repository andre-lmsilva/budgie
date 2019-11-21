package io.geekmind.budgie.balance.commons;

import io.geekmind.budgie.model.dto.standard_account.ExistingStandardAccount;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface BalanceDatesCalculator {
    /**
     * Calculates the final date of a period based on the account starting day of the month and a reference date.
     *
     * @param referenceDate Reference date to be used to calculate the final date of the period.
     * @param account       Account to have the final date for the calculated period.
     * @return              The final date for the period.
     */
    LocalDate calculatePeriodEndDate(LocalDate referenceDate, ExistingStandardAccount account);

    /**
     * Calculates the period start date based purely on the already calculated period end date and
     * the assumption that a period always comprehend a month in time.
     *
     * @param periodEndDate Already calculated period final date.
     * @return  Period start date.
     */
    LocalDate calculatePeriodStartDate(LocalDate periodEndDate);

    /**
     * Calculates the billing date for the period based on the already calculated period final date and the account
     * settings.
     *
     * @param periodEndDate Already calculated period final date.
     * @param account       Account which period is being calculated.
     * @return The calculated billing date for the account in the period.
     */
    LocalDate calculatePeriodBillingDate(LocalDate periodEndDate, ExistingStandardAccount account);

    /**
     * Calculates how many days until the period end date, starting from current date.
     *
     * @param periodEndDate Calculated period final date.
     * @return Number of the days left until the end of period. If the end of the period occurs before the
     * current date, returns 0 (zero).
     */
    Integer calculateDaysUntilEndOfPeriod(LocalDate periodEndDate);

    /**
     * Calculates the period end date for an account based on the calculated billing date.
     *
     * @param billingDate   Billing date to be used to calculates the period end date.
     * @param account       Account having its end period date calculated.
     * @return Calculated period end date.
     */
    LocalDate calculatePeriodEndDateBasedOnBillingDate(LocalDate billingDate, ExistingStandardAccount account);

    /**
     * Calculates, in terms of percentage, how much of the period is already past. If the <i>daysUntilEndOfPeriod</i>
     * is equal to zero, this method will returns one hundred. If the <i>daysUntilEndOfPeriod</i> is greater than thirty
     * one, this method will returns zero. Otherwise, it will (1) calculate how many days is comprehended between the
     * 
     * @param periodStartDate       Start date of the period being calculated.
     * @param periodEndDate         End date of the period being calculated.
     * @param daysUntilEndOfPeriod  Number of days between today and the end of the period.
     * @return Percentage of completion for the period being calculated.
     */
    BigDecimal calculatePeriodCompletion(LocalDate periodStartDate, LocalDate periodEndDate, Integer daysUntilEndOfPeriod);
}
