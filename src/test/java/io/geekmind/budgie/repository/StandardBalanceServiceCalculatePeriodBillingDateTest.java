package io.geekmind.budgie.repository;

import io.geekmind.budgie.fixture.ExistingAccountFixture;
import io.geekmind.budgie.model.dto.ExistingAccount;
import org.junit.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class StandardBalanceServiceCalculatePeriodBillingDateTest {

    private StandardBalanceService balanceService = new StandardBalanceService(null, null, null, null);

    @Test
    public void calculatePeriodBillingDate_WhenTheBillingDateOccursBeforeThePeriodEndDateDay_AdvanceOneMonth() {
        ExistingAccount fakeAccount = ExistingAccountFixture.getMainAccount();
        fakeAccount.setMonthBillingDayAt(5);
        LocalDate periodEndDate = LocalDate.of(2019, 1, 10);

        LocalDate result = this.balanceService.calculatePeriodBillingDate(fakeAccount, periodEndDate);

        assertThat(result.getMonthValue()).isEqualTo(periodEndDate.plusMonths(1L).getMonthValue());
    }

    @Test
    public void calculatePeriodBillingDate_WhenTheBillingDateOccursAfterThePeriodEndDateDay_DoesNotAdvanceOneMonth() {
        ExistingAccount fakeAccount = ExistingAccountFixture.getMainAccount();
        fakeAccount.setMonthBillingDayAt(5);
        LocalDate periodEndDate = LocalDate.of(2019, 1, 1);

        LocalDate result = this.balanceService.calculatePeriodBillingDate(fakeAccount, periodEndDate);

        assertThat(result.getMonthValue()).isEqualTo(periodEndDate.getMonthValue());
    }

    @Test
    public void calculatePeriodBillingDate_WhenTheBillingDayOccursAfterTheEndOfMonth_SetsTheDateAsLastDayOfMonth() {
        ExistingAccount fakeAccount = ExistingAccountFixture.getMainAccount();
        fakeAccount.setMonthBillingDayAt(31);
        LocalDate periodEndDate = LocalDate.of(2019, 2, 1);

        LocalDate result = this.balanceService.calculatePeriodBillingDate(fakeAccount, periodEndDate);

        assertThat(result.getDayOfMonth()).isEqualTo(periodEndDate.lengthOfMonth());
    }

    @Test
    public void calculatePeriodBillingDate_WhenTheBillingDayOccursBeforeTheEndOfMonth_SetsTheCorrectBillingDay() {
        ExistingAccount fakeAccount = ExistingAccountFixture.getMainAccount();
        fakeAccount.setMonthBillingDayAt(20);
        LocalDate periodEndDate = LocalDate.of(2019, 2, 1);

        LocalDate result = this.balanceService.calculatePeriodBillingDate(fakeAccount, periodEndDate);

        assertThat(result.getDayOfMonth()).isEqualTo(20);
    }
}
