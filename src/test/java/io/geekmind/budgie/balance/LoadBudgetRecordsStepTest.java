package io.geekmind.budgie.balance;

import io.geekmind.budgie.fixture.BalanceCalculationRequestFixture;
import io.geekmind.budgie.fixture.ExistingRecordFixture;
import io.geekmind.budgie.model.dto.BalanceCalculationRequest;
import io.geekmind.budgie.model.dto.ExistingRecord;
import io.geekmind.budgie.repository.BudgetTemplateRecordService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class LoadBudgetRecordsStepTest {

    @Mock
    private BudgetTemplateRecordService budgetTemplateRecordService;

    @InjectMocks
    private LoadBudgetRecordsStep step;

    @Test
    public void shouldExecute_withNullRequest_ReturnsFalse() {
        assertThat(step.shouldExecute(null)).isFalse();
    }

    @Test
    public void shouldExecute_withNullBalance_ReturnsFalse() {
        assertThat(step.shouldExecute(new BalanceCalculationRequest())).isFalse();
    }

    @Test
    public void shouldExecute_withNullAccount_ReturnsFalse() {
        BalanceCalculationRequest request = BalanceCalculationRequestFixture.get();
        request.getBalance().setAccount(null);
        assertThat(this.step.shouldExecute(request)).isFalse();
    }

    @Test
    public void shouldExecute_withNullRecords_ReturnsFalse() {
        BalanceCalculationRequest request = BalanceCalculationRequestFixture.get();
        request.getBalance().setRecords(null);
        assertThat(this.step.shouldExecute(request)).isFalse();
    }

    @Test
    public void shouldExecute_withNullBalanceDates_ReturnsFalse() {
        BalanceCalculationRequest request = BalanceCalculationRequestFixture.get();
        request.getBalance().setBalanceDates(null);
        assertThat(this.step.shouldExecute(request)).isFalse();
    }

    @Test
    public void shouldExecute_withNullPeriodEndDate_ReturnsFalse() {
        BalanceCalculationRequest request = BalanceCalculationRequestFixture.get();
        request.getBalance().getBalanceDates().setPeriodEndDate(null);
        assertThat(this.step.shouldExecute(request)).isFalse();
    }

    @Test
    public void shouldExecute_withPeriodEndDateInThePast_ReturnsFalse() {
        BalanceCalculationRequest request = BalanceCalculationRequestFixture.get();
        request.getBalance().getBalanceDates().setPeriodEndDate(LocalDate.now().minusDays(1));
        assertThat(this.step.shouldExecute(request)).isFalse();
    }

    @Test
    public void shouldExecute_withAccountAndRecordsAndPeriodEndDateNotInThePast_ReturnsTrue() {
        BalanceCalculationRequest request = BalanceCalculationRequestFixture.get();
        assertThat(this.step.shouldExecute(request)).isTrue();
    }

    @Test
    public void calculate_withNoRecordWithContainerIdAndNoBudgetTemplate_SetsEmptyList() {
        BalanceCalculationRequest request = BalanceCalculationRequestFixture.get();
        request.getBalance().getRecords().add(ExistingRecordFixture.getWithValue(BigDecimal.TEN));
        request.getBalance().setApplicableBudgetTemplateRecords(null);
        request.getBalance().getRecords().get(0).setContainerId(null);
        doReturn(Collections.emptyList())
            .when(this.budgetTemplateRecordService).loadAllFromAccount(eq(request.getBalance().getAccount().getId()));

        this.step.calculate(request);

        assertThat(request.getBalance().getApplicableBudgetTemplateRecords()).isEmpty();
    }

    @Test
    public void calculate_withNoRecordWithContainerIdAndBudgetTemplate_ReturnsNonEmptyList() {
        BalanceCalculationRequest request = BalanceCalculationRequestFixture.get();
        request.getBalance().getRecords().add(ExistingRecordFixture.getWithValue(BigDecimal.TEN));
        request.getBalance().setApplicableBudgetTemplateRecords(null);
        request.getBalance().getRecords().get(0).setContainerId(null);

        ExistingRecord budgetTemplateRecord = ExistingRecordFixture.getWithValue(BigDecimal.TEN);
        budgetTemplateRecord.setContainerId(-9);
        doReturn(Collections.singletonList(budgetTemplateRecord))
            .when(this.budgetTemplateRecordService).loadAllFromAccount(eq(request.getBalance().getAccount().getId()));

        this.step.calculate(request);

        assertThat(request.getBalance().getApplicableBudgetTemplateRecords())
            .containsOnly(budgetTemplateRecord);
    }

    @Test
    public void calculate_withRecordContainingContainerIdDifferentFromBudgetTemplateRecord_ReturnsNonEmptyList() {
        BalanceCalculationRequest request = BalanceCalculationRequestFixture.get();
        request.getBalance().getRecords().add(ExistingRecordFixture.getWithValue(BigDecimal.TEN));
        request.getBalance().setApplicableBudgetTemplateRecords(null);
        request.getBalance().getRecords().get(0).setContainerId(-8);

        ExistingRecord budgetTemplateRecord = ExistingRecordFixture.getWithValue(BigDecimal.TEN);
        budgetTemplateRecord.setContainerId(-9);
        doReturn(Collections.singletonList(budgetTemplateRecord))
            .when(this.budgetTemplateRecordService).loadAllFromAccount(eq(request.getBalance().getAccount().getId()));

        this.step.calculate(request);

        assertThat(request.getBalance().getApplicableBudgetTemplateRecords())
            .containsOnly(budgetTemplateRecord);
    }

    @Test
    public void calculate_withRecordContainingContainerIdEqualsToBudgetTemplateRecord_ReturnsEmptyList() {
        BalanceCalculationRequest request = BalanceCalculationRequestFixture.get();
        request.getBalance().getRecords().add(ExistingRecordFixture.getWithValue(BigDecimal.TEN));
        request.getBalance().setApplicableBudgetTemplateRecords(null);
        request.getBalance().getRecords().get(0).setContainerId(-9);

        ExistingRecord budgetTemplateRecord = ExistingRecordFixture.getWithValue(BigDecimal.TEN);
        budgetTemplateRecord.setContainerId(-9);
        doReturn(Collections.singletonList(budgetTemplateRecord))
            .when(this.budgetTemplateRecordService).loadAllFromAccount(eq(request.getBalance().getAccount().getId()));

        this.step.calculate(request);

        assertThat(request.getBalance().getApplicableBudgetTemplateRecords()).isEmpty();
    }

}