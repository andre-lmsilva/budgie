package io.geekmind.budgie.balance;

import io.geekmind.budgie.model.dto.balance.Balance;
import io.geekmind.budgie.model.dto.balance.BalanceCalculationRequest;
import io.geekmind.budgie.model.dto.balance.CategoryBalanceSummary;
import io.geekmind.budgie.model.dto.DependantAccountRecord;
import io.geekmind.budgie.model.dto.ExistingRecord;
import io.geekmind.budgie.model.dto.category.ExistingCategory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Calculates the expenses in a period grouping by its categories.
 *
 * @author Andre Silva
 */
@Component("summarizeExpensesPerCategoryStep")
public class SummarizeExpensesPerCategoryStep extends BaseBalanceCalculationStep {

    public SummarizeExpensesPerCategoryStep(@Qualifier("loadBudgetRecordsStep") BaseBalanceCalculationStep nextChainedStep) {
        super(nextChainedStep);
    }

    /**
     * Joins the records of the balance with the records of the dependant account balances (if present) and
     * invokes the {@link #summarizeExpensesPerCategory(List, BigDecimal)} to summarize the values. The result
     * will be set on {@link Balance#setCategoryBalanceSummaries(List)}.
     *
     * @param balanceCalculationRequest Instance of the balance calculation request being processed.
     */
    @Override
    public void calculate(BalanceCalculationRequest balanceCalculationRequest) {
        List<ExistingRecord> records = balanceCalculationRequest.getBalance()
                .getRecords()
                .stream()
                .filter(record -> !(record instanceof DependantAccountRecord))
                .collect(Collectors.toList());
        if (null != balanceCalculationRequest.getDependantAccountBalances()) {
            for (Balance balance : balanceCalculationRequest.getDependantAccountBalances().values()) {
                records.addAll(balance.getRecords());
            }
        }
        balanceCalculationRequest.getBalance().setCategoryBalanceSummaries(
            this.summarizeExpensesPerCategory(
                records,
                balanceCalculationRequest.getBalance().getSummary().getTotalExpenses()
            )
        );
    }

    /**
     * Groups the records by its category, invokes the {@link #createSummary(ExistingCategory, BigDecimal, BigDecimal)}
     * to map the result into a {@link CategoryBalanceSummary} instance, filters to remove zero valued balances and sort
     * it descending by value.
     * @param records           Records within the period being calculated.
     * @param totalExpenses     Total of expenses in the period being calculated.
     * @return List containing the expenses grouped by category.
     */
    protected List<CategoryBalanceSummary> summarizeExpensesPerCategory(List<ExistingRecord> records, BigDecimal totalExpenses) {
        return records
            .stream()
            .collect(
                Collectors.groupingBy(ExistingRecord::getCategory)
            )
            .entrySet()
            .stream()
            .map(entry -> this.createSummary(entry.getKey(), entry.getValue(), totalExpenses))
            .filter(summary -> !summary.getBalance().equals(BigDecimal.ZERO))
            .sorted(Comparator.comparing(CategoryBalanceSummary::getBalance))
            .collect(Collectors.toList());
    }

    /**
     * Based on an existing category, the sum of all expenses classified in the period for this category and the sum of
     * all expenses in the period, returns a new instance ok {@link CategoryBalanceSummary} containing the calculated
     * values properly filled in the attributes.
     *
     * @param category                  Category having its records summarized for the period.
     * @param records                   Records assigned to the received category.
     * @param totalExpensesInBalance    Sum of all expense records in the period.
     * @return A new instance of {@link CategoryBalanceSummary} filled with the calculated summary values.
     */
    protected CategoryBalanceSummary createSummary(ExistingCategory category, List<ExistingRecord> records, BigDecimal totalExpensesInBalance) {
        BigDecimal maxExpenses = BigDecimal.ZERO;
        BigDecimal maxExpensesConsumption = BigDecimal.ZERO;

        Map<String, BigDecimal> balanceBreakDown = records
            .stream()
            .collect(
                Collectors.groupingBy(
                    (ExistingRecord record) -> record.getAccount().getName(),
                    Collectors.reducing(BigDecimal.ZERO, ExistingRecord::getRecordValue, BigDecimal::add)
                )
            );

        BigDecimal balance = balanceBreakDown.values()
            .stream()
            .reduce(BigDecimal.ZERO, BigDecimal::add);


        if (null != category.getMaxExpenses() && category.getMaxExpenses().compareTo(BigDecimal.ZERO) != 0) {
            maxExpenses = category.getMaxExpenses();
            maxExpensesConsumption = balance.divide(maxExpenses, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100D));
        }

        BigDecimal expensesConsumptionPercentage = BigDecimal.ZERO;
        if (totalExpensesInBalance.compareTo(BigDecimal.ZERO) != 0) {
            expensesConsumptionPercentage = balance.divide(totalExpensesInBalance, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100D));
        }

        return new CategoryBalanceSummary(
            category,
            balance,
            expensesConsumptionPercentage,
            maxExpensesConsumption,
            maxExpenses,
            balanceBreakDown
        );
    }

    @Override
    public Boolean shouldExecute(BalanceCalculationRequest balanceCalculationRequest) {
        return null != balanceCalculationRequest &&
                null != balanceCalculationRequest.getBalance() &&
                null != balanceCalculationRequest.getBalance().getRecords() &&
                null != balanceCalculationRequest.getBalance().getSummary() &&
                null != balanceCalculationRequest.getBalance().getSummary().getTotalExpenses();
    }
}
