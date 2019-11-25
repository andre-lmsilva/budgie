package io.geekmind.budgie.balance;

import io.geekmind.budgie.fixture.BalanceCalculationRequestFixture;
import io.geekmind.budgie.fixture.BalanceFixture;
import io.geekmind.budgie.fixture.ExistingStandardAccountFixture;
import io.geekmind.budgie.fixture.ExistingCategoryFixture;
import io.geekmind.budgie.fixture.ExistingRecordFixture;
import io.geekmind.budgie.model.dto.balance.Balance;
import io.geekmind.budgie.model.dto.balance.BalanceCalculationRequest;
import io.geekmind.budgie.model.dto.balance.CategoryBalanceSummary;
import io.geekmind.budgie.model.dto.category.ExistingCategory;
import io.geekmind.budgie.model.dto.ExistingRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class SummarizeExpensesPerCategoryStepTest {

    @Spy
    @InjectMocks
    private SummarizeExpensesPerCategoryStep step;

    @Test
    public void shouldExecute_withNullRequest_ReturnsFalse() {
        assertThat(step.shouldExecute(null)).isFalse();
    }

    @Test
    public void shouldExecute_withNullBalance_ReturnsFalse() {
        assertThat(step.shouldExecute(new BalanceCalculationRequest())).isFalse();
    }

    @Test
    public void shouldExecute_withNullRecords_ReturnsFalse() {
        BalanceCalculationRequest request = BalanceCalculationRequestFixture.get();
        request.getBalance().setRecords(null);
        assertThat(step.shouldExecute(request)).isFalse();
    }

    @Test
    public void shouldExecute_withNonNullRecords_ReturnsTrue() {
        BalanceCalculationRequest request = BalanceCalculationRequestFixture.get();
        assertThat(this.step.shouldExecute(request)).isTrue();
    }

    @Test
    public void createSummary_withNoDefinedMaxExpensesValue_FillsTheMaxExpensesConsumptionWithZero() {
        ExistingCategory category = ExistingCategoryFixture.get();
        category.setMaxExpenses(null);
        BigDecimal balance = BigDecimal.valueOf(10D).negate();
        BigDecimal totalExpenses = BigDecimal.valueOf(100D).negate();

        ExistingRecord singleRecord = ExistingRecordFixture.getWithValue(balance);
        List<ExistingRecord> records = Collections.singletonList(singleRecord);

        CategoryBalanceSummary result = this.step.createSummary(category, records, totalExpenses);

        assertThat(result)
            .hasFieldOrPropertyWithValue("category", category)
            .hasFieldOrPropertyWithValue("balance", balance)
            .hasFieldOrPropertyWithValue("expensesConsumptionPercentage", balance.divide(totalExpenses, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100D)))
            .hasFieldOrPropertyWithValue("maxExpensesConsumption", BigDecimal.ZERO)
            .hasFieldOrPropertyWithValue("maxExpenses", BigDecimal.ZERO);

        assertThat(result.getBalanceBreakDown())
            .containsEntry(singleRecord.getAccount().getName(), singleRecord.getRecordValue());
    }

    @Test
    public void createSummary_withDefinedMaxExpensesValue_FillTheMaxExpensesConsumptionWithTheConsumptionPercentage() {
        ExistingCategory category = ExistingCategoryFixture.get();
        category.setMaxExpenses(BigDecimal.valueOf(20D).negate());
        BigDecimal balance = BigDecimal.valueOf(10D).negate();
        BigDecimal totalExpenses = BigDecimal.valueOf(100D).negate();

        List<ExistingRecord> records = Collections.singletonList(
            ExistingRecordFixture.getWithValue(balance)
        );

        CategoryBalanceSummary result = this.step.createSummary(category, records, totalExpenses);

        assertThat(result)
            .hasFieldOrPropertyWithValue("category", category)
            .hasFieldOrPropertyWithValue("balance", balance)
            .hasFieldOrPropertyWithValue("expensesConsumptionPercentage", balance.divide(totalExpenses, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100D)))
            .hasFieldOrPropertyWithValue("maxExpensesConsumption", balance.divide(category.getMaxExpenses(), RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100D)))
            .hasFieldOrPropertyWithValue("maxExpenses", category.getMaxExpenses());
    }

    @Test
    public void createSummary_withRecordsOfMultipleAccounts_FillTheBalanceBreakdownGroupingByAccount() {
        ExistingCategory category = ExistingCategoryFixture.get();
        category.setMaxExpenses(BigDecimal.valueOf(20D).negate());
        BigDecimal totalExpenses = BigDecimal.valueOf(100D).negate();

        ExistingRecord mainAccountRecord = ExistingRecordFixture.getWithValue(BigDecimal.valueOf(100D).negate());
        ExistingRecord savingsAccountRecord = ExistingRecordFixture.getWithValue(BigDecimal.valueOf(200D).negate());
        savingsAccountRecord.setAccount(ExistingStandardAccountFixture.getSavingsAccount());

        List<ExistingRecord> records = Arrays.asList(mainAccountRecord, savingsAccountRecord);

        CategoryBalanceSummary result = this.step.createSummary(category, records, totalExpenses);

        assertThat(result.getBalance())
            .isEqualTo(mainAccountRecord.getRecordValue().add(savingsAccountRecord.getRecordValue()));

        assertThat(result.getBalanceBreakDown())
            .hasSize(2)
            .containsEntry(mainAccountRecord.getAccount().getName(), mainAccountRecord.getRecordValue())
            .containsEntry(savingsAccountRecord.getAccount().getName(), savingsAccountRecord.getRecordValue());
    }

    @Test
    public void summarizeExpensesPerCategory_withRecordsOfASingleCategory_ReturnsSummaryForThatCategory() {
        ExistingRecord firstRecord = ExistingRecordFixture.getWithValue(BigDecimal.valueOf(50D).negate());
        ExistingRecord secondRecord = ExistingRecordFixture.getWithValue(BigDecimal.valueOf(40D).negate());
        BigDecimal totalExpenses = BigDecimal.valueOf(90D).negate();

        CategoryBalanceSummary fakeSummary = new CategoryBalanceSummary();
        fakeSummary.setBalance(BigDecimal.TEN);
        doReturn(fakeSummary)
            .when(this.step).createSummary(
                eq(firstRecord.getCategory()),
                anyList(),
                eq(totalExpenses)
        );

        List<CategoryBalanceSummary> result = this.step.summarizeExpensesPerCategory(
            Arrays.asList(firstRecord, secondRecord),
            totalExpenses
        );

        assertThat(result).containsOnly(fakeSummary);
        verify(this.step).createSummary(
            eq(firstRecord.getCategory()),
            anyList(),
            eq(totalExpenses)
        );
    }

    @Test
    public void summarizeExpensesPerCategory_withMultipleCategories_ReturnsARecordForEachCategory() {
        ExistingRecord firstRecord = ExistingRecordFixture.getWithValue(BigDecimal.valueOf(10D).negate());
        firstRecord.setCategory(ExistingCategoryFixture.get());
        ExistingRecord secondRecord = ExistingRecordFixture.getWithValue(BigDecimal.valueOf(20D).negate());
        secondRecord.setCategory(ExistingCategoryFixture.getFuel());

        BigDecimal totalExpenses = BigDecimal.valueOf(90D).negate();

        CategoryBalanceSummary firstSummary = new CategoryBalanceSummary();
        firstSummary.setBalance(BigDecimal.TEN);
        doReturn(firstSummary)
            .when(this.step).createSummary(
                eq(firstRecord.getCategory()),
                anyList(),
                eq(totalExpenses)
        );

        CategoryBalanceSummary secondSummary = new CategoryBalanceSummary();
        secondSummary.setBalance(BigDecimal.ONE);
        doReturn(secondSummary)
            .when(this.step).createSummary(
                eq(secondRecord.getCategory()),
                anyList(),
                eq(totalExpenses)
        );

        List<CategoryBalanceSummary> result = this.step.summarizeExpensesPerCategory(
            Arrays.asList(firstRecord, secondRecord),
            totalExpenses
        );

        assertThat(result).containsExactly(secondSummary, firstSummary);
        verify(this.step).createSummary(
            eq(firstRecord.getCategory()),
            anyList(),
            eq(totalExpenses)
        );

        verify(this.step).createSummary(
            eq(secondRecord.getCategory()),
            anyList(),
            eq(totalExpenses)
        );
    }

    @Test
    public void summarizeExpensesPerCategory_withZeroValuedSummary_DoesNotReturnTheSummary() {
        ExistingRecord firstRecord = ExistingRecordFixture.getWithValue(BigDecimal.valueOf(50D).negate());
        ExistingRecord secondRecord = ExistingRecordFixture.getWithValue(BigDecimal.valueOf(40D).negate());
        BigDecimal totalExpenses = BigDecimal.valueOf(90D).negate();

        CategoryBalanceSummary fakeSummary = new CategoryBalanceSummary();
        fakeSummary.setBalance(BigDecimal.ZERO);
        doReturn(fakeSummary)
            .when(this.step).createSummary(
                eq(firstRecord.getCategory()),
                anyList(),
                eq(totalExpenses)
        );

        List<CategoryBalanceSummary> result = this.step.summarizeExpensesPerCategory(
            Arrays.asList(firstRecord, secondRecord),
            totalExpenses
        );

        assertThat(result).isEmpty();
    }

    @Test
    public void calculate_withNoDependantAccountBalances_SummarizesOnlyBalanceRecords() {
        BalanceCalculationRequest request = BalanceCalculationRequestFixture.get();
        List<CategoryBalanceSummary> fakeResult = Collections.emptyList();
        doReturn(fakeResult)
            .when(this.step).summarizeExpensesPerCategory(eq(request.getBalance().getRecords()), any());

        this.step.calculate(request);

        assertThat(request.getBalance().getCategoryBalanceSummaries()).isSameAs(fakeResult);
    }

    @Test
    public void calculate_withDependantAccountBalances_SummarizeBalanceRecordsAndDependantAccountBalanceRecords() {
        BalanceCalculationRequest request = BalanceCalculationRequestFixture.get();
        request.setDependantAccountBalances(new HashMap<>());
        Balance dependantAccountBalance = BalanceFixture.getCurrentPeriodBalance();
        request.getDependantAccountBalances().put(dependantAccountBalance.getAccount(), dependantAccountBalance);
        List<CategoryBalanceSummary> fakeResult = Collections.emptyList();
        doReturn(fakeResult)
            .when(this.step).summarizeExpensesPerCategory(any(), any());

        this.step.calculate(request);

        ArgumentCaptor<List<ExistingRecord>> recordsCaptor = ArgumentCaptor.forClass(List.class);
        verify(this.step).summarizeExpensesPerCategory(recordsCaptor.capture(), any());
        assertThat(recordsCaptor.getValue())
            .containsAll(request.getBalance().getRecords())
            .containsAll(dependantAccountBalance.getRecords());
    }

}