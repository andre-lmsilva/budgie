package io.geekmind.budgie.balance;

import io.geekmind.budgie.balance.commons.BalanceDatesCalculator;
import io.geekmind.budgie.fixture.BalanceCalculationRequestFixture;
import io.geekmind.budgie.fixture.BalanceFixture;
import io.geekmind.budgie.fixture.ExistingStandardAccountFixture;
import io.geekmind.budgie.model.dto.balance.Balance;
import io.geekmind.budgie.model.dto.balance.BalanceCalculationRequest;
import io.geekmind.budgie.model.dto.balance.BalanceDates;
import io.geekmind.budgie.model.dto.balance.BalanceType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class CalculateRegularBalanceDatesStepTest {

    @Mock
    private BalanceDatesCalculator datesCalculator;

    @InjectMocks
    private CalculateRegularBalanceDatesStep step;

    private Balance balance;
    private LocalDate periodStartDate;
    private LocalDate periodEndDate;
    private LocalDate periodBillingDate;
    private int daysUntilEndOfPeriod;
    private BigDecimal periodCompletion;

    @Before
    public void setUp() {
        this.periodStartDate = LocalDate.now().withDayOfMonth(1);
        this.periodEndDate = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
        this.periodBillingDate = this.periodEndDate.plusDays(5);
        this.daysUntilEndOfPeriod = this.periodEndDate.getDayOfMonth() - LocalDate.now().getDayOfMonth();

        double totalDaysInThePeriod = (double) ChronoUnit.DAYS.between(this.periodStartDate, this.periodEndDate);
        double totalPastDaysInThePeriod = totalDaysInThePeriod - this.daysUntilEndOfPeriod;
        this.periodCompletion = BigDecimal.valueOf(totalPastDaysInThePeriod)
            .divide(BigDecimal.valueOf(totalDaysInThePeriod), RoundingMode.CEILING)
            .multiply(BigDecimal.valueOf(100D));

        BalanceDates balanceDates = new BalanceDates();
        balanceDates.setReferenceDate(LocalDate.now());
        this.balance = BalanceFixture.getCurrentPeriodBalance();
        balance.setBalanceDates(balanceDates);
        balance.setAccount(ExistingStandardAccountFixture.getMainAccount());

        doReturn(this.periodEndDate)
            .when(this.datesCalculator).calculatePeriodEndDate(eq(balanceDates.getReferenceDate()), eq(balance.getAccount()));
        doReturn(this.periodStartDate)
            .when(this.datesCalculator).calculatePeriodStartDate(eq(this.periodEndDate));
        doReturn(this.periodBillingDate)
            .when(this.datesCalculator).calculatePeriodBillingDate(eq(this.periodEndDate), eq(balance.getAccount()));
        doReturn(this.daysUntilEndOfPeriod)
            .when(this.datesCalculator).calculateDaysUntilEndOfPeriod(eq(this.periodEndDate));
        doReturn(this.periodCompletion)
            .when(this.datesCalculator).calculatePeriodCompletion(eq(this.periodStartDate), eq(this.periodEndDate), eq(this.daysUntilEndOfPeriod));

        BalanceCalculationRequest request = new BalanceCalculationRequest();
        request.setBalance(this.balance);
        this.step.calculate(request);
    }

    @Test
    public void balanceDatesFilledWithReferenceDate() {
        assertThat(this.balance.getBalanceDates())
            .hasFieldOrPropertyWithValue("referenceDate", LocalDate.now());
    }

    @Test
    public void balanceDatesFilledWithPeriodStartDate() {
        assertThat(this.balance.getBalanceDates())
            .hasFieldOrPropertyWithValue("periodStartDate", this.periodStartDate);
    }

    @Test
    public void balanceDatesFilledWithPeriodEndDate() {
        assertThat(this.balance.getBalanceDates())
            .hasFieldOrPropertyWithValue("periodEndDate", this.periodEndDate);
    }

    @Test
    public void balanceDatesFilledWithPeriodBillingDate() {
        assertThat(this.balance.getBalanceDates())
            .hasFieldOrPropertyWithValue("periodBillingDate", this.periodBillingDate);
    }

    @Test
    public void balanceDatesFilledWithPreviousPeriodStartDate() {
        assertThat(this.balance.getBalanceDates())
            .hasFieldOrPropertyWithValue("previousPeriodStartDate", this.periodStartDate.minusMonths(1));
    }

    @Test
    public void balanceDatesFilledWithNextPeriodStartDate() {
        assertThat(this.balance.getBalanceDates())
            .hasFieldOrPropertyWithValue("nextPeriodStartDate", this.periodStartDate.plusMonths(1));
    }

    @Test
    public void balanceDatesFilledWithPeriodRemainingDays() {
         assertThat(this.balance.getBalanceDates())
            .hasFieldOrPropertyWithValue("periodRemainingDays", this.daysUntilEndOfPeriod);
    }

    @Test
    public void balanceDatesFilledWithPeriodCompletion() {
        assertThat(this.balance.getBalanceDates())
            .hasFieldOrPropertyWithValue("periodCompletion", this.periodCompletion);
    }

    @Test
    public void shouldExecute_withBalanceCalculationRequestNull_ReturnsFalse() {
        assertThat(this.step.shouldExecute(null)).isFalse();
    }

    @Test
    public void shouldExecute_withNullBalance_ReturnsFalse() {
        assertThat(this.step.shouldExecute(new BalanceCalculationRequest())).isFalse();
    }

    @Test
    public void shouldExecute_withBudgetBalanceType_ReturnsFalse() {
        BalanceCalculationRequest request = BalanceCalculationRequestFixture.get();
        request.getBalance().setBalanceType(BalanceType.BUDGET_TEMPLATE_BALANCE);
        assertThat(this.step.shouldExecute(request))
            .isFalse();
    }

    @Test
    public void shouldExecute_withNullBalanceDates_ReturnsFalse() {
        BalanceCalculationRequest request = new BalanceCalculationRequest();
        request.setBalance(new Balance());
        assertThat(this.step.shouldExecute(request)).isFalse();
    }

    @Test
    public void shouldExecute_withNullReferenceDate_ReturnsFalse() {
        Balance balance = BalanceFixture.getCurrentPeriodBalance();
        balance.getBalanceDates().setReferenceDate(null);
        BalanceCalculationRequest request = new BalanceCalculationRequest();
        request.setBalance(balance);

        assertThat(this.step.shouldExecute(request)).isFalse();
    }

    @Test
    public void shouldExecute_withNullAccount_ReturnsFalse() {
        Balance balance = BalanceFixture.getCurrentPeriodBalance();
        balance.setAccount(null);
        BalanceCalculationRequest request = new BalanceCalculationRequest();
        request.setBalance(balance);
        assertThat(this.step.shouldExecute(request)).isFalse();
    }

    @Test
    public void shouldExecute_withReferenceDateAndAccount_ReturnsTrue() {
        Balance balance = BalanceFixture.getCurrentPeriodBalance();
        BalanceCalculationRequest request = new BalanceCalculationRequest();
        request.setBalance(balance);
        assertThat(this.step.shouldExecute(request)).isTrue();
    }

}