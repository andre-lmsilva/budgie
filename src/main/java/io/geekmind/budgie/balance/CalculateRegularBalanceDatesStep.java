package io.geekmind.budgie.balance;

import io.geekmind.budgie.balance.commons.BalanceDatesCalculator;
import io.geekmind.budgie.model.dto.balance.BalanceCalculationRequest;
import io.geekmind.budgie.model.dto.balance.BalanceDates;
import io.geekmind.budgie.model.dto.balance.BalanceType;
import io.geekmind.budgie.model.dto.standard_account.ExistingStandardAccount;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Calculates the following balance related dates:
 *
 * <ul>
 *     <li>Period start date.</li>
 *     <li>Period end date.</li>
 *     <li>Period billing date.</li>
 *     <li>Previous period start date.</li>
 *     <li>Next period start date.</li>
 *     <li>Number of days remaining until the end of period.</li>
 *     <li>Period completion percentage.</li>
 * </ul>
 *
 * Those dates are filled into a {@link BalanceDates} instance and set on the balance being calculated.
 *
 * @author Andre Silva
 */
@Component("balanceCalculationChainEntryPoint")
public class CalculateRegularBalanceDatesStep extends BaseBalanceCalculationStep{

    private final BalanceDatesCalculator dateCalculator;

    public CalculateRegularBalanceDatesStep(@Qualifier("calculateBudgetBalanceDatesStep") BaseBalanceCalculationStep nextChainedStep,
                                            BalanceDatesCalculator dateCalculator) {
        super(nextChainedStep);
        this.dateCalculator = dateCalculator;
    }

    @Override
    public void calculate(BalanceCalculationRequest balanceCalculationRequest) {
        LocalDate referenceDate = balanceCalculationRequest.getBalance().getBalanceDates().getReferenceDate();
        ExistingStandardAccount account = balanceCalculationRequest.getBalance().getAccount();

        LocalDate periodEndDate = this.dateCalculator.calculatePeriodEndDate(referenceDate, account);
        LocalDate periodStartDate = this.dateCalculator.calculatePeriodStartDate(periodEndDate);
        LocalDate previousPeriodStartDate = periodStartDate.minusMonths(1);
        LocalDate nextPeriodStartDate = periodStartDate.plusMonths(1);
        LocalDate periodBillingDate = this.dateCalculator.calculatePeriodBillingDate(periodEndDate, account);
        Integer daysUntilEndOfPeriod = this.dateCalculator.calculateDaysUntilEndOfPeriod(periodEndDate);
        BigDecimal periodCompletion = this.dateCalculator.calculatePeriodCompletion(
            periodStartDate,
            periodEndDate,
            daysUntilEndOfPeriod
        );

        balanceCalculationRequest.getBalance().setBalanceDates(
            new BalanceDates(
                referenceDate,
                periodStartDate,
                periodEndDate,
                periodBillingDate,
                previousPeriodStartDate,
                nextPeriodStartDate,
                daysUntilEndOfPeriod,
                periodCompletion
            )
        );
    }

    @Override
    public Boolean shouldExecute(BalanceCalculationRequest balanceCalculationRequest) {
        return balanceCalculationRequest != null &&
                balanceCalculationRequest.getBalance() != null &&
                balanceCalculationRequest.getBalance().getBalanceType().equals(BalanceType.REGULAR_PERIOD_BALANCE) &&
                balanceCalculationRequest.getBalance().getBalanceDates() != null &&
                balanceCalculationRequest.getBalance().getBalanceDates().getReferenceDate() != null &&
                balanceCalculationRequest.getBalance().getAccount() != null;
    }
}
