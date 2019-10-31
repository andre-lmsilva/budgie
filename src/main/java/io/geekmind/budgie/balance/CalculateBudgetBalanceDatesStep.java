package io.geekmind.budgie.balance;

import io.geekmind.budgie.model.dto.BalanceCalculationRequest;
import io.geekmind.budgie.model.dto.BalanceDates;
import io.geekmind.budgie.model.dto.BalanceType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Fills the balance dates with the dates to calculate the budget template balance. Due the nature of the artificial
 * dates defined for budget template records, the balance dates are filled with the following constant values:
 *
 * <ul>
 *     <li>{@link BalanceDates#setReferenceDate(LocalDate)} filled with {@link #BEGINNING_OF_PERIOD}</li>
 *     <li>{@link BalanceDates#setPeriodStartDate(LocalDate)} (LocalDate)} filled with {@link #BEGINNING_OF_PERIOD}</li>
 *     <li>{@link BalanceDates#setPeriodEndDate(LocalDate)} (LocalDate)} filled with {@link #END_OF_PERIOD}</li>
 *     <li>{@link BalanceDates#setPeriodBillingDate(LocalDate)} (LocalDate)} filled with {@link #BEGINNING_OF_PERIOD}</li>
 *     <li>{@link BalanceDates#setPreviousPeriodStartDate(LocalDate)} (LocalDate)} (LocalDate)} filled with {@link #BEGINNING_OF_PERIOD}</li>
 *     <li>{@link BalanceDates#setNextPeriodStartDate(LocalDate)} (LocalDate)} (LocalDate)} filled with {@link #BEGINNING_OF_PERIOD}</li>
 *     <li>{@link BalanceDates#setPeriodRemainingDays(Integer)} (LocalDate)} filled with {@link #PERIOD_REMAINING_DAYS}</li>
 * </ul>
 *
 * @author Andre Silva
 */
@Component("calculateBudgetBalanceDatesStep")
public class CalculateBudgetBalanceDatesStep extends BaseBalanceCalculationStep {

    public static final LocalDate BEGINNING_OF_PERIOD = LocalDate.of(9999, 12, 1);
    public static final LocalDate END_OF_PERIOD = LocalDate.of(9999, 12, 31);
    public static final Integer PERIOD_REMAINING_DAYS = 31;

    public CalculateBudgetBalanceDatesStep(@Qualifier("loadPeriodRecordsStep") BaseBalanceCalculationStep nextChainedStep) {
        super(nextChainedStep);
    }

    @Override
    public void calculate(BalanceCalculationRequest balanceCalculationRequest) {
        balanceCalculationRequest.getBalance().setBalanceDates(
            new BalanceDates(
                BEGINNING_OF_PERIOD,
                BEGINNING_OF_PERIOD,
                END_OF_PERIOD,
                BEGINNING_OF_PERIOD,
                BEGINNING_OF_PERIOD,
                BEGINNING_OF_PERIOD,
                PERIOD_REMAINING_DAYS
            )
        );
    }

    @Override
    public Boolean shouldExecute(BalanceCalculationRequest balanceCalculationRequest) {
        return balanceCalculationRequest != null &&
                balanceCalculationRequest.getBalance() != null &&
                balanceCalculationRequest.getBalance().getBalanceType() != null &&
                balanceCalculationRequest.getBalance().getBalanceType().equals(BalanceType.BUDGET_TEMPLATE_BALANCE);
    }
}