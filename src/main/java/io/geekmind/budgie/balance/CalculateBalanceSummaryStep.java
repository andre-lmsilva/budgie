package io.geekmind.budgie.balance;

import io.geekmind.budgie.model.dto.BalanceCalculationRequest;
import io.geekmind.budgie.model.dto.BalanceSummary;
import io.geekmind.budgie.model.dto.ExistingRecord;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;

/**
 * Summarize the record values within the period and calculates the following values:
 *
 * <ul>
 *     <li>Total of income.</li>
 *     <li>Total of expenses.</li>
 *     <li>Final period balance.</li>
 *     <li>Period balance to date.</li>
 * </ul>
 *
 * @author Andre Silva
 */
@Component("calculateBalanceSummaryStep")
public class CalculateBalanceSummaryStep extends BaseBalanceCalculationStep {

    /**
     * Provides a predicate that will filter a list of {@link ExistingRecord} returning only the records with a positive
     * record value.
     */
    public static final Predicate<ExistingRecord> INCOMES_RECORDS_FILTER = (existingRecord ->
        null != existingRecord.getRecordValue() && existingRecord.getRecordValue().compareTo(BigDecimal.ZERO) > 0
    );

    /**
     * Provides a predicate that will filter a list of {@link ExistingRecord} returning only the record with a negative
     * record value.
     */
    public static final Predicate<ExistingRecord> EXPENSES_RECORDS_FILTER = (existingRecord ->
        null != existingRecord.getRecordValue() && existingRecord.getRecordValue().compareTo(BigDecimal.ZERO) < 0
    );

    /**
     * This is a dummy filter that always returns true, and it is intended to be used when filtering the records for
     * the entire period. It takes as assumption that the records where already filtered by date on
     * {@link LoadPeriodRecordsStep}.
     */
    public static final Predicate<ExistingRecord> ENTIRE_PERIOD_FILTER = (existingRecord -> Boolean.TRUE);

    /**
     * Provides a predicate that will filter a list of {@link ExistingRecord} returning any record which record value is
     * not null.
     */
    public static final Predicate<ExistingRecord> ANY_RECORDS_FILTER = (existingRecord -> null != existingRecord.getRecordValue());

    public CalculateBalanceSummaryStep(@Qualifier("summarizeExpensesPerCategoryStep") BaseBalanceCalculationStep nextChainedStep) {
        super(nextChainedStep);
    }

    /**
     * Calculates the summary values of the balance for the account in the period.
     * @param balanceCalculationRequest Instance of the balance calculation request being processed.
     */
    @Override
    public void calculate(BalanceCalculationRequest balanceCalculationRequest) {
        List<ExistingRecord> records = balanceCalculationRequest.getBalance().getRecords();

        LocalDate today = LocalDate.now();
        Predicate<ExistingRecord> upToDateFilter = (existingRecord -> existingRecord.getRecordDate().compareTo(today) <= 0);

        BigDecimal finalBalance = this.summarizeRecords(records, ANY_RECORDS_FILTER, ENTIRE_PERIOD_FILTER);
        BigDecimal totalIncomes = this.summarizeRecords(records, INCOMES_RECORDS_FILTER, ENTIRE_PERIOD_FILTER);
        BigDecimal totalExpenses = this.summarizeRecords(records, EXPENSES_RECORDS_FILTER, ENTIRE_PERIOD_FILTER);
        BigDecimal balanceUpToDate = this.summarizeRecords(records, ANY_RECORDS_FILTER, upToDateFilter);

        balanceCalculationRequest.getBalance().setSummary(
            new BalanceSummary(finalBalance, totalIncomes, totalExpenses, balanceUpToDate)
        );
    }

    protected BigDecimal summarizeRecords(final List<ExistingRecord> records,
                                          final Predicate<ExistingRecord> valueFilter,
                                          final Predicate<ExistingRecord> periodFilter) {
         return BigDecimal.valueOf(
             records.stream()
                 .filter(periodFilter)
                 .filter(valueFilter)
                 .map(ExistingRecord::getRecordValue)
                 .mapToDouble(BigDecimal::doubleValue)
                 .sum()
         );
    }

    @Override
    public Boolean shouldExecute(BalanceCalculationRequest balanceCalculationRequest) {
        return null != balanceCalculationRequest &&
                null != balanceCalculationRequest.getBalance() &&
                null != balanceCalculationRequest.getBalance().getRecords();
    }
}
