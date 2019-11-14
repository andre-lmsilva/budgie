package io.geekmind.budgie.balance;

import io.geekmind.budgie.fixture.BalanceCalculationRequestFixture;
import io.geekmind.budgie.fixture.BalanceFixture;
import io.geekmind.budgie.fixture.ExistingStandardAccountFixture;
import io.geekmind.budgie.model.dto.Balance;
import io.geekmind.budgie.model.dto.BalanceCalculationRequest;
import io.geekmind.budgie.model.dto.BalanceType;
import io.geekmind.budgie.model.dto.DependantAccountRecord;
import ma.glasnost.orika.MapperFacade;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(MockitoJUnitRunner.class)
public class GenerateDependantAccountRecordsStepTest {

    @Mock
    private MapperFacade mapper;

    @Spy
    @InjectMocks
    private GenerateDependantAccountRecordsStep step;

    @Test
    public void shouldExecute_withNullRequest_ReturnsFalse() {
        assertThat(this.step.shouldExecute(null)).isFalse();
    }

    @Test
    public void shouldExecute_withNullBalance_ReturnsFalse() {
        assertThat(this.step.shouldExecute(new BalanceCalculationRequest())).isFalse();
    }

    @Test
    public void shouldExecute_withNullBalanceType_ReturnsFalse() {
        BalanceCalculationRequest request = BalanceCalculationRequestFixture.get();
        request.getBalance().setBalanceType(null);
        assertThat(this.step.shouldExecute(request)).isFalse();
    }

    @Test
    public void shouldExecute_withBudgetBalanceType_ReturnsFalse() {
        BalanceCalculationRequest request = BalanceCalculationRequestFixture.get();
        request.getBalance().setBalanceType(BalanceType.BUDGET_TEMPLATE_BALANCE);
        assertThat(this.step.shouldExecute(request)).isFalse();
    }

    @Test
    public void shouldExecute_withNullAccount_ReturnsFalse() {
        BalanceCalculationRequest request = BalanceCalculationRequestFixture.get();
        request.getBalance().setAccount(null);
        assertThat(this.step.shouldExecute(request)).isFalse();
    }

    @Test
    public void shouldExecute_withNonMainAccount_ReturnsFalse() {
        BalanceCalculationRequest request = BalanceCalculationRequestFixture.get();
        request.getBalance().getAccount().setMainAccount(false);
        assertThat(this.step.shouldExecute(request)).isFalse();
    }

    @Test
    public void shouldExecute_withNullDependantAccountBalances_ReturnsFalse() {
        BalanceCalculationRequest request = BalanceCalculationRequestFixture.get();
        request.getBalance().getAccount().setMainAccount(true);
        request.setDependantAccountBalances(null);
        assertThat(this.step.shouldExecute(request)).isFalse();
    }

    @Test
    public void shouldExecute_withEmptyDependantAccountBalances_ReturnsFalse() {
        BalanceCalculationRequest request = BalanceCalculationRequestFixture.get();
        request.getBalance().getAccount().setMainAccount(true);
        request.setDependantAccountBalances(new HashMap<>());
        assertThat(this.step.shouldExecute(request)).isFalse();
    }

    @Test
    public void shouldExecute_withNonEmptyAccountBalancesAndMainAccount_ReturnsTrue() {
        BalanceCalculationRequest request = BalanceCalculationRequestFixture.get();
        request.getBalance().getAccount().setMainAccount(true);
        request.setDependantAccountBalances(new HashMap<>());
        request.getDependantAccountBalances().put(ExistingStandardAccountFixture.getMainAccount(), BalanceFixture.getCurrentPeriodBalance());
        assertThat(this.step.shouldExecute(request)).isTrue();
    }

    @Test
    public void calculate_withOnlyBalancesEqualsToZero_DoesNotPerformAnyMap() {
        Balance fakeBalance = BalanceFixture.getCurrentPeriodBalance();
        fakeBalance.getSummary().setFinalBalance(BigDecimal.ZERO);
        BalanceCalculationRequest request = BalanceCalculationRequestFixture.get();
        request.setDependantAccountBalances(new HashMap<>());
        request.getDependantAccountBalances().put(fakeBalance.getAccount(), fakeBalance);

        this.step.calculate(request);

        verifyZeroInteractions(this.mapper);
    }

    @Test
    public void calculate_withNonZeroBalances_PerformMapping() {
        Balance fakeBalance = BalanceFixture.getCurrentPeriodBalance();
        BalanceCalculationRequest request = BalanceCalculationRequestFixture.get();
        request.setDependantAccountBalances(new HashMap<>());
        request.getDependantAccountBalances().put(
            fakeBalance.getAccount(),
            fakeBalance
        );
        DependantAccountRecord fakeDependantAccountRecord = new DependantAccountRecord();
        doReturn(fakeDependantAccountRecord)
            .when(this.mapper).map(eq(fakeBalance), eq(DependantAccountRecord.class));

        this.step.calculate(request);
        assertThat(request.getBalance().getRecords()).contains(fakeDependantAccountRecord);
    }

}