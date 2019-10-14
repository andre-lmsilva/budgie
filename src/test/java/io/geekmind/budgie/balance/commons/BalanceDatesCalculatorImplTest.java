package io.geekmind.budgie.balance.commons;

import io.geekmind.budgie.model.dto.ExistingAccount;
import org.junit.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class BalanceDatesCalculatorImplTest {

    private BalanceDatesCalculator calculator = new BalanceDatesCalculatorImpl();

    @Test
    public void calculatePeriodEndDate_withMonthStartingDayAsTheFirstDayOfMonth_ReturnsLastDayOfMonth() {
        ExistingAccount account = new ExistingAccount();
        account.setMonthStartingAt(1);
        LocalDate referenceDate = LocalDate.of(2019, 10, 10);

        LocalDate result = this.calculator.calculatePeriodEndDate(referenceDate, account);

        assertThat(result).isEqualTo(
            LocalDate.of(2019, 10, 31)
        );
    }

    @Test
    public void calculatePeriodEndDate_withMonthStartingDayAsTheLastDayOfMonth_ReturnsLastDayOfMonthMinusOne() {
        ExistingAccount account = new ExistingAccount();
        account.setMonthStartingAt(31);
        LocalDate referenceDate = LocalDate.of(2019, 10, 10);

        LocalDate result = this.calculator.calculatePeriodEndDate(referenceDate, account);

        assertThat(result).isEqualTo(
            LocalDate.of(2019, 10, 30)
        );
    }

    @Test
    public void calculatePeriodEndDate_withMonthStartingDayAsTheLastDayOfMonthWithShorterMonthAsReferenceDate_ReturnsLastDayOfMonthMinusOne() {
        ExistingAccount account = new ExistingAccount();
        account.setMonthStartingAt(31);
        LocalDate referenceDate = LocalDate.of(2019, 2, 10);

        LocalDate result = this.calculator.calculatePeriodEndDate(referenceDate, account);

        assertThat(result).isEqualTo(
            LocalDate.of(2019, 2, 27)
        );
    }
    @Test
    public void calculatePeriodEndDate_withMonthStatingDaysInTheMiddleOfMonthAndReferenceDateInTheSameMonth_ReturnsEndDayInSameMonth() {
        ExistingAccount account = new ExistingAccount();
        account.setMonthStartingAt(28);
        LocalDate referenceDate = LocalDate.of(2019, 10, 10);

        LocalDate result = this.calculator.calculatePeriodEndDate(referenceDate, account);

        assertThat(result).isEqualTo(
            LocalDate.of(2019, 10, 27)
        );
    }

    @Test
    public void calculatePeriodEndDate_withMonthStatingDaysInTheMiddleOfMonthAndReferenceDateInThePreviousMonth_ReturnsEndDayOfNextMonth() {
        ExistingAccount account = new ExistingAccount();
        account.setMonthStartingAt(28);
        LocalDate referenceDate = LocalDate.of(2019, 9, 28);

        LocalDate result = this.calculator.calculatePeriodEndDate(referenceDate, account);

        assertThat(result).isEqualTo(
            LocalDate.of(2019, 10, 27)
        );
    }

    @Test
    public void calculatePeriodStartDate_withPeriodEndDateAsLastDayOfMonth_ReturnsTheFirstDayOfTheSameMonth() {
        LocalDate periodEndDate = LocalDate.of(2019, 1, 31);

        LocalDate result = this.calculator.calculatePeriodStartDate(periodEndDate);

        assertThat(result).isEqualTo(
            LocalDate.of(2019, 1, 1)
        );
    }

    @Test
    public void calculatePeriodStartDate_withPeriodEndDateAsAnyDayOfMonth_ReturnsTheSameDayOfPreviousMonthMinusOne() {
        LocalDate periodEndDate = LocalDate.of(2019, 1, 28);

        LocalDate result = this.calculator.calculatePeriodStartDate(periodEndDate);

        assertThat(result).isEqualTo(
            LocalDate.of(2018, 12, 29)
        );
    }

    @Test
    public void calculatePeriodStartDate_withPeriodEndDateAsFirstDayOfMonth_ReturnsTheSecondDayOfPreviousMonth() {
        LocalDate periodEndDate = LocalDate.of(2019, 1, 1);

        LocalDate result = this.calculator.calculatePeriodStartDate(periodEndDate);

        assertThat(result).isEqualTo(
            LocalDate.of(2018, 12, 2)
        );
    }

    @Test
    public void calculatePeriodBillingDate_withMonthBillingDayBeforeThePeriodEndDateDay_ReturnsBillingDateOnNextMonth() {
        LocalDate periodEndDate = LocalDate.of(2019, 1, 5);
        ExistingAccount account = new ExistingAccount();
        account.setMonthBillingDayAt(3);

        LocalDate result = this.calculator.calculatePeriodBillingDate(periodEndDate, account);

        assertThat(result).isEqualTo(
            LocalDate.of(2019, 2, 3)
        );
    }

    @Test
    public void calculatePeriodBillingDate_withMonthBillingDayEqualsToThePeriodEndDateDay_ReturnsBillingDateOnSameMonth() {
        LocalDate periodEndDate = LocalDate.of(2019, 1, 5);
        ExistingAccount account = new ExistingAccount();
        account.setMonthBillingDayAt(5);

        LocalDate result = this.calculator.calculatePeriodBillingDate(periodEndDate, account);

        assertThat(result).isEqualTo(
            LocalDate.of(2019, 1, 5)
        );
    }

    @Test
    public void calculatePeriodBillingDate_withMonthBillingDayGreaterThenPeriodEndDateDay_ReturnsBillingDateOnSameMonth() {
        LocalDate periodEndDate = LocalDate.of(2019, 1, 5);
        ExistingAccount account = new ExistingAccount();
        account.setMonthBillingDayAt(10);

        LocalDate result = this.calculator.calculatePeriodBillingDate(periodEndDate, account);

        assertThat(result).isEqualTo(
            LocalDate.of(2019, 1, 10)
        );
    }

    @Test
    public void calculatePeriodBillingDate_withMonthBillingDayAfterLastDayOfMonth_ReturnsBillingDateAsLastDayOfMonth() {
        LocalDate periodEndDate = LocalDate.of(2019, 2, 5);
        ExistingAccount account = new ExistingAccount();
        account.setMonthBillingDayAt(31);

        LocalDate result = this.calculator.calculatePeriodBillingDate(periodEndDate, account);

        assertThat(result).isEqualTo(
            LocalDate.of(2019, 2, 28)
        );
    }

    @Test
    public void calculateDaysUntilEndOfPeriod_withPeriodEndDateBeforeCurrentDate_ReturnsZero() {
        LocalDate periodEndDate = LocalDate.now().minusMonths(1);
        Integer result = this.calculator.calculateDaysUntilEndOfPeriod(periodEndDate);
        assertThat(result).isEqualTo(0);
    }

    @Test
    public void calculateDaysUntilEndOfPeriod_withPeriodEndDateAfterCurrentDate_ReturnsNumberOfDaysUntilPeriodEndDate() {
        LocalDate periodEndDate = LocalDate.now().plusDays(5);
        Integer result = this.calculator.calculateDaysUntilEndOfPeriod(periodEndDate);
        assertThat(result).isEqualTo(5);
    }

    @Test
    public void calculatePeriodEndDateBasedOnBillingDate_withRegularBillingDate_ReturnsPreviousMonthPeriodEndDate() {
        LocalDate billingDate = LocalDate.of(2019, 1, 5);
        ExistingAccount account = new ExistingAccount();
        account.setMonthStartingAt(10);

        LocalDate result = this.calculator.calculatePeriodEndDateBasedOnBillingDate(billingDate, account);

        assertThat(result).isEqualTo(
            LocalDate.of(2018, 12, 9)
        );
    }

    @Test
    public void calculatePeriodEndDateBasedOnBillingDate_withMonthStatingDayAfterEndOfMonth_ReturnsPreviousMonthLastDayMinusOne() {
        LocalDate billingDate = LocalDate.of(2019, 3, 5);
        ExistingAccount account = new ExistingAccount();
        account.setMonthStartingAt(30);

        LocalDate result = this.calculator.calculatePeriodEndDateBasedOnBillingDate(billingDate, account);

        assertThat(result).isEqualTo(
            LocalDate.of(2019, 2, 27)
        );
    }

}