package io.geekmind.budgie.balance;

import io.geekmind.budgie.fixture.BalanceCalculationRequestFixture;
import io.geekmind.budgie.fixture.ExistingRecordFixture;
import io.geekmind.budgie.model.dto.balance.BalanceCalculationRequest;
import io.geekmind.budgie.model.dto.ExistingRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class CalculateBalanceSummaryStepTest {

    @Spy
    @InjectMocks
    private CalculateBalanceSummaryStep step;

    @Test
    public void shouldExecute_withNullRequest_ReturnsFalse() {
        assertThat(this.step.shouldExecute(null)).isFalse();
    }

    @Test
    public void shouldExecute_withNullBalance_ReturnsFalse() {
        assertThat(this.step.shouldExecute(new BalanceCalculationRequest())).isFalse();
    }

    @Test
    public void shouldExecute_withNoRecordsAndNoDependantAccountBalances_ReturnsFalse() {
        BalanceCalculationRequest request = BalanceCalculationRequestFixture.get();
        request.setDependantAccountBalances(null);
        request.getBalance().setRecords(null);
        assertThat(this.step.shouldExecute(request)).isFalse();
    }

    @Test
    public void shouldExecute_withRecordsAndBalanceDates_ReturnsTrue() {
        BalanceCalculationRequest request = BalanceCalculationRequestFixture.get();
        assertThat(this.step.shouldExecute(request)).isTrue();
    }

    @Test
    public void incomesRecordsFilter_withBalanceRecords_ReturnsOnlyPositiveValuedRecords() {
        ExistingRecord incomeRecord = ExistingRecordFixture.getWithValue(BigDecimal.TEN);
        ExistingRecord expenseRecord = ExistingRecordFixture.getWithValue(BigDecimal.TEN.negate());

        List<ExistingRecord> result = Arrays.asList(incomeRecord, expenseRecord)
            .stream()
            .filter(CalculateBalanceSummaryStep.INCOMES_RECORDS_FILTER)
            .collect(Collectors.toList());

        assertThat(result).containsOnly(incomeRecord);
    }

    @Test
    public void expensesRecordsFilter_withBalanceRecords_ReturnsOnlyNegativeValuedRecords() {
        ExistingRecord incomeRecord = ExistingRecordFixture.getWithValue(BigDecimal.TEN);
        ExistingRecord expenseRecord = ExistingRecordFixture.getWithValue(BigDecimal.TEN.negate());

        List<ExistingRecord> result = Arrays.asList(incomeRecord, expenseRecord)
            .stream()
            .filter(CalculateBalanceSummaryStep.EXPENSES_RECORDS_FILTER)
            .collect(Collectors.toList());

        assertThat(result).containsOnly(expenseRecord);
    }

    @Test
    public void entirePeriodFilter_withBalanceRecords_ReturnsAllRecordsDespiteOfValueAndDate() {
        ExistingRecord incomeRecord = ExistingRecordFixture.getWithValue(BigDecimal.TEN);
        ExistingRecord expenseRecord = ExistingRecordFixture.getWithValue(BigDecimal.TEN.negate());

        List<ExistingRecord> result = Arrays.asList(incomeRecord, expenseRecord)
            .stream()
            .filter(CalculateBalanceSummaryStep.ENTIRE_PERIOD_FILTER)
            .collect(Collectors.toList());

        assertThat(result).containsOnly(incomeRecord, expenseRecord);
    }

    @Test
    public void anyRecordFilter_withBalanceRecords_ReturnsAnyRecordWithNonNullRecordValue() {
        ExistingRecord nullValuedRecord = new ExistingRecord();
        ExistingRecord expenseRecord = ExistingRecordFixture.getWithValue(BigDecimal.TEN.negate());

        List<ExistingRecord> result = Arrays.asList(nullValuedRecord, expenseRecord)
            .stream()
            .filter(CalculateBalanceSummaryStep.ANY_RECORDS_FILTER)
            .collect(Collectors.toList());

        assertThat(result).containsOnly(expenseRecord);
    }

    @Test
    public void summarizeRecords_withBalanceRecordsValueFilterAndPeriodFilter_ReturnsSummarizedRecordValue() {
        ExistingRecord firstIncomeRecord = ExistingRecordFixture.getWithValue(BigDecimal.TEN);
        ExistingRecord secondIncomeRecord = ExistingRecordFixture.getWithValue(BigDecimal.ONE);
        ExistingRecord firstExpenseRecord = ExistingRecordFixture.getWithValue(BigDecimal.TEN.negate());

        BigDecimal result = this.step.summarizeRecords(
            Arrays.asList(firstExpenseRecord, firstIncomeRecord, secondIncomeRecord),
            CalculateBalanceSummaryStep.INCOMES_RECORDS_FILTER,
            CalculateBalanceSummaryStep.ENTIRE_PERIOD_FILTER
        );

        assertThat(result).isEqualTo(BigDecimal.valueOf(11D));
    }

    @Test
    public void calculate_withBalanceRecords_FillBalanceSummary() {
        doReturn(BigDecimal.valueOf(300D))
            .when(this.step).summarizeRecords(anyList(), eq(CalculateBalanceSummaryStep.INCOMES_RECORDS_FILTER), eq(CalculateBalanceSummaryStep.ENTIRE_PERIOD_FILTER));
        doReturn(BigDecimal.valueOf(100D).negate())
            .when(this.step).summarizeRecords(anyList(), eq(CalculateBalanceSummaryStep.EXPENSES_RECORDS_FILTER), eq(CalculateBalanceSummaryStep.ENTIRE_PERIOD_FILTER));
        doReturn(BigDecimal.valueOf(200D))
            .doReturn(BigDecimal.valueOf(300D))
            .when(this.step).summarizeRecords(anyList(), eq(CalculateBalanceSummaryStep.ANY_RECORDS_FILTER), any());

        BalanceCalculationRequest request = BalanceCalculationRequestFixture.get();
        this.step.calculate(request);

        assertThat(request.getBalance().getSummary())
            .hasFieldOrPropertyWithValue("finalBalance", BigDecimal.valueOf(200D))
            .hasFieldOrPropertyWithValue("totalIncomes", BigDecimal.valueOf(300D))
            .hasFieldOrPropertyWithValue("totalExpenses", BigDecimal.valueOf(100D).negate())
            .hasFieldOrPropertyWithValue("balanceUpToDate", BigDecimal.valueOf(300D));
    }

}