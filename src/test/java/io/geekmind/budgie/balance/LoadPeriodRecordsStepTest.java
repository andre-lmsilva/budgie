package io.geekmind.budgie.balance;

import io.geekmind.budgie.fixture.BalanceFixture;
import io.geekmind.budgie.model.dto.balance.Balance;
import io.geekmind.budgie.model.dto.balance.BalanceCalculationRequest;
import io.geekmind.budgie.model.dto.ExistingRecord;
import io.geekmind.budgie.repository.RecordService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class LoadPeriodRecordsStepTest {

    @Mock
    private RecordService recordService;

    @InjectMocks
    private LoadPeriodRecordsStep step;

    @Test
    public void calculate_withBalanceDatesAlreadyCalculated_RetrieveTheRecordsWithingThePeriod() {
        Balance fakeBalance = BalanceFixture.getCurrentPeriodBalance();
        List<ExistingRecord> fakeRecords = Collections.emptyList();
        doReturn(fakeRecords)
            .when(this.recordService).loadAll(
                eq(fakeBalance.getAccount().getId()),
                eq(fakeBalance.getBalanceDates().getPeriodStartDate()),
                eq(fakeBalance.getBalanceDates().getPeriodEndDate())
            );

        BalanceCalculationRequest request = new BalanceCalculationRequest();
        request.setBalance(fakeBalance);

        this.step.calculate(request);

        assertThat(fakeBalance.getRecords()).isSameAs(fakeRecords);
    }

    @Test
    public void shouldExecute_withNullBalance_ReturnsFalse() {
        assertThat(this.step.shouldExecute(null)).isFalse();
    }

    @Test
    public void shouldExecute_withNullBalanceDates_ReturnsFalse() {
        assertThat(this.step.shouldExecute(new BalanceCalculationRequest())).isFalse();
    }

    @Test
    public void shouldExecute_withNullPeriodStartDate_ReturnsFalse() {
        Balance balance = BalanceFixture.getCurrentPeriodBalance();
        balance.getBalanceDates().setPeriodStartDate(null);
        BalanceCalculationRequest request = new BalanceCalculationRequest();
        request.setBalance(balance);
        assertThat(this.step.shouldExecute(request)).isFalse();
    }

    @Test
    public void shouldExecute_withNullPeriodEndDate_ReturnsFalse() {
        Balance balance = BalanceFixture.getCurrentPeriodBalance();
        balance.getBalanceDates().setPeriodEndDate(null);
        BalanceCalculationRequest request = new BalanceCalculationRequest();
        request.setBalance(balance);
        assertThat(this.step.shouldExecute(request)).isFalse();
    }

    @Test
    public void shouldExecute_withPeriodStartDateAndPeriodEndDateCalculated_ReturnsTrue() {
        Balance balance = BalanceFixture.getCurrentPeriodBalance();
        BalanceCalculationRequest request = new BalanceCalculationRequest();
        request.setBalance(balance);
        assertThat(this.step.shouldExecute(request)).isTrue();
    }

}