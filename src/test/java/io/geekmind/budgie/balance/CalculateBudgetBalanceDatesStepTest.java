package io.geekmind.budgie.balance;

import io.geekmind.budgie.fixture.BalanceCalculationRequestFixture;
import io.geekmind.budgie.model.dto.Balance;
import io.geekmind.budgie.model.dto.BalanceCalculationRequest;
import io.geekmind.budgie.model.dto.BalanceType;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CalculateBudgetBalanceDatesStepTest {

    private CalculateBudgetBalanceDatesStep step = new CalculateBudgetBalanceDatesStep(null);

    @Test
    public void shouldExecute_withNullBalanceCalculationRequest_ReturnsFalse() {
        assertThat(step.shouldExecute(null)).isFalse();
    }

    @Test
    public void shouldExecute_withNullBalance_ReturnsFalse() {
        assertThat(step.shouldExecute(new BalanceCalculationRequest())).isFalse();
    }

    @Test
    public void shouldExecute_withNullBalanceType_ReturnsFalse() {
        BalanceCalculationRequest request = BalanceCalculationRequestFixture.get();
        request.getBalance().setBalanceType(null);
        assertThat(step.shouldExecute(request)).isFalse();
    }

    @Test
    public void shouldExecute_withRegularBalanceType_ReturnsFalse() {
        BalanceCalculationRequest request = BalanceCalculationRequestFixture.get();
        request.getBalance().setBalanceType(BalanceType.REGULAR_PERIOD_BALANCE);
        assertThat(step.shouldExecute(request)).isFalse();
    }

    @Test
    public void shouldExecute_witBudgetBalanceType_ReturnsTrue() {
        BalanceCalculationRequest request = BalanceCalculationRequestFixture.get();
        request.getBalance().setBalanceType(BalanceType.BUDGET_TEMPLATE_BALANCE);
        assertThat(step.shouldExecute(request)).isTrue();
    }

    @Test
    public void calculate_withValidRequest_FillsBalanceDatesWithPredefinedValues() {
        BalanceCalculationRequest request = new BalanceCalculationRequest();
        request.setBalance(new Balance());

        this.step.calculate(request);

        assertThat(request.getBalance().getBalanceDates())
            .isNotNull()
            .hasFieldOrPropertyWithValue("referenceDate", CalculateBudgetBalanceDatesStep.BEGINNING_OF_PERIOD)
            .hasFieldOrPropertyWithValue("periodStartDate", CalculateBudgetBalanceDatesStep.BEGINNING_OF_PERIOD)
            .hasFieldOrPropertyWithValue("periodEndDate", CalculateBudgetBalanceDatesStep.END_OF_PERIOD)
            .hasFieldOrPropertyWithValue("periodBillingDate", CalculateBudgetBalanceDatesStep.BEGINNING_OF_PERIOD)
            .hasFieldOrPropertyWithValue("previousPeriodStartDate", CalculateBudgetBalanceDatesStep.BEGINNING_OF_PERIOD)
            .hasFieldOrPropertyWithValue("nextPeriodStartDate", CalculateBudgetBalanceDatesStep.BEGINNING_OF_PERIOD)
            .hasFieldOrPropertyWithValue("periodRemainingDays", CalculateBudgetBalanceDatesStep.PERIOD_REMAINING_DAYS);
    }

}