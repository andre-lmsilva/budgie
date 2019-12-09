package io.geekmind.budgie.balance;

import io.geekmind.budgie.fixture.BalanceCalculationRequestFixture;
import io.geekmind.budgie.fixture.ExistingProjectAccountFixture;
import io.geekmind.budgie.fixture.ExistingRecordFixture;
import io.geekmind.budgie.model.dto.ExistingRecord;
import io.geekmind.budgie.model.dto.balance.BalanceCalculationRequest;
import io.geekmind.budgie.model.dto.project_account.ExistingProjectAccount;
import io.geekmind.budgie.repository.RecordService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CalculateProjectSummariesStepTest {

    @Mock
    private RecordService recordService;

    @Spy
    @InjectMocks
    private CalculateProjectSummariesStep step;

    @Test
    public void shouldExecute_withAccountWithoutAssociatedActiveProjectAccounts_ReturnsFalse() {
        BalanceCalculationRequest request = BalanceCalculationRequestFixture.get();
        request.getBalance().getAccount().setDependants(null);

        boolean result = this.step.shouldExecute(request);

        assertThat(result).isFalse();
    }

    @Test
    public void shouldExecute_withAccountContainingAssociatedActiveProjectAccounts_ReturnsTrue() {
        BalanceCalculationRequest request = BalanceCalculationRequestFixture.get();
        request.getBalance().getAccount().setDependants(new ArrayList<>());
        request.getBalance().getAccount().getDependants().add(ExistingProjectAccountFixture.travelToBahamas());

        boolean result = this.step.shouldExecute(request);

        assertThat(result).isTrue();
    }

    @Test
    public void calculateBalanceFor_withAccountWithoutRecordsOnTheBalanceInterval_ReturnsZero() {
        ExistingProjectAccount projectAccount = ExistingProjectAccountFixture.travelToBahamas();
        doReturn(Collections.emptyList())
            .when(this.recordService).loadAll(anyInt(), any(LocalDate.class), any(LocalDate.class));

        BigDecimal result = this.step.calculateBalanceFor(projectAccount, LocalDate.now(), LocalDate.now().plusMonths(1L));

        assertThat(result).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    public void calculateBalanceFor_withAccountWithRecordsOnTheBalanceInterval_ReturnsRecordValuesSummarized() {
        ExistingProjectAccount projectAccount = ExistingProjectAccountFixture.travelToBahamas();
        List<ExistingRecord> recordList = new ArrayList<>();
        recordList.add(ExistingRecordFixture.getWithValue(BigDecimal.valueOf(100D)));
        recordList.add(ExistingRecordFixture.getWithValue(BigDecimal.valueOf(-250D)));

        doReturn(recordList)
                .when(this.recordService).loadAll(anyInt(), any(LocalDate.class), any(LocalDate.class));

        BigDecimal result = this.step.calculateBalanceFor(projectAccount, LocalDate.now(), LocalDate.now().plusMonths(1L));

        assertThat(result).isEqualTo(BigDecimal.valueOf(-150D));
    }

    @Test
    public void calculateEndDate_withPeriodEndDateOccurringInTheFuture_ReturnsCurrentDate() {
        BalanceCalculationRequest request = BalanceCalculationRequestFixture.get();
        LocalDate periodEndDate = request.getBalance().getBalanceDates().getPeriodEndDate().plusMonths(1L);
        request.getBalance().getBalanceDates().setPeriodEndDate(periodEndDate);

        LocalDate result = this.step.calculateEndDate(request);

        assertThat(result).isEqualTo(LocalDate.now());
    }

    @Test
    public void calculateEndDate_withPeriodEndDateInThePresent_ReturnsCurrentDate() {
        BalanceCalculationRequest request = BalanceCalculationRequestFixture.get();
        request.getBalance().getBalanceDates().setPeriodEndDate(LocalDate.now());

        LocalDate result = this.step.calculateEndDate(request);

        assertThat(result).isEqualTo(LocalDate.now());
    }

    @Test
    public void calculateEndDate_withPeriodEndDateOccurringInThePast_ReturnsBalancePeriodEndDate() {
        BalanceCalculationRequest request = BalanceCalculationRequestFixture.get();
        LocalDate periodEndDate = LocalDate.now().minusDays(1L);
        request.getBalance().getBalanceDates().setPeriodEndDate(periodEndDate);

        LocalDate result = this.step.calculateEndDate(request);

        assertThat(result).isEqualTo(periodEndDate);
    }

    @Test
    public void calculate_withNoActiveProjectAccount_DoesNotPerformAnyCalculation() {
        BalanceCalculationRequest request = BalanceCalculationRequestFixture.get();
        request.getBalance().getAccount().setDependants(Collections.emptyList());
        doReturn(LocalDate.now())
            .when(this.step).calculateEndDate(eq(request));

        this.step.calculate(request);

        verify(this.step).calculateEndDate(eq(request));
        verify(this.step, never()).calculateBalanceFor(
            any(),
            eq(CalculateProjectSummariesStep.START_DATE),
            eq(LocalDate.now())
        );
        assertThat(request.getBalance().getProjectBalanceSummaries()).isEmpty();
    }

    @Test
    public void calculate_withActiveProjectAccount_FillsProjectBalanceSummaries() {
        BalanceCalculationRequest request = BalanceCalculationRequestFixture.get();
        ExistingProjectAccount projectAccount = ExistingProjectAccountFixture.travelToBahamas();
        projectAccount.setTargetValue(BigDecimal.valueOf(100D));
        request.getBalance().getAccount().setDependants(
            Collections.singletonList(projectAccount)
        );
        doReturn(LocalDate.now())
            .when(this.step).calculateEndDate(eq(request));
        doReturn(BigDecimal.valueOf(10D))
            .when(this.step).calculateBalanceFor(
                eq(projectAccount),
                eq(CalculateProjectSummariesStep.START_DATE),
                eq(LocalDate.now())
            );

        this.step.calculate(request);

        verify(this.step).calculateEndDate(eq(request));
        verify(this.step).calculateBalanceFor(
            eq(projectAccount),
            eq(CalculateProjectSummariesStep.START_DATE),
            eq(LocalDate.now())
        );

        assertThat(request.getBalance().getProjectBalanceSummaries())
            .hasSize(1)
            .element(0)
            .hasFieldOrPropertyWithValue("project", projectAccount)
            .hasFieldOrPropertyWithValue("balance", BigDecimal.valueOf(10D))
            .hasFieldOrPropertyWithValue("progressPercentage", BigDecimal.valueOf(10D).setScale(2, RoundingMode.HALF_UP));
    }

}