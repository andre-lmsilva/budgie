package io.geekmind.budgie.repository;

import io.geekmind.budgie.model.dto.ExistingAccount;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class StandardBalanceServiceCalculatePeriodEndDateTest {

    @InjectMocks
    protected StandardBalanceService balanceService;

    @Test
    public void calculatePeriodEndDate_WithAccountStartingPeriodOnFirstOfEachMonth_ReturnsLastDayOfMonth() {
        LocalDate referenceDate = LocalDate.parse("19/06/2019", DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        ExistingAccount fakeAccount = new ExistingAccount();
        fakeAccount.setMonthStartingAt(1);

        LocalDate result = this.balanceService.calculatePeriodEndDate(fakeAccount, referenceDate);

        assertThat(result.getDayOfMonth()).isEqualTo(referenceDate.lengthOfMonth());
    }

    @Test
    public void calculatePeriodEndDate_WithAccountStartingPeriodOnAnyOtherDay_ReturnsOneDayBefore() {
        LocalDate referenceDate = LocalDate.parse("19/06/2019", DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        ExistingAccount fakeAccount = new ExistingAccount();
        fakeAccount.setMonthStartingAt(15);

        LocalDate result = this.balanceService.calculatePeriodEndDate(fakeAccount, referenceDate);

        assertThat(result.getDayOfMonth()).isEqualTo(14);
    }

    @Test
    public void calculatePeriodEndDate_WhenReferenceDateDayIsAfterTheBeginningOfPeriod_AdvancesOneMonth() {
        LocalDate referenceDate = LocalDate.parse("19/06/2019", DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        ExistingAccount fakeAccount = new ExistingAccount();
        fakeAccount.setMonthStartingAt(15);

        LocalDate result = this.balanceService.calculatePeriodEndDate(fakeAccount, referenceDate);

        assertThat(result.getMonthValue())
            .isEqualTo(referenceDate.plusMonths(1L).getMonthValue());
    }

    @Test
    public void calculatePeriodEndDate_WhenReferenceDayIsBeforeBeginningOfPeriod_KeepsSameMonthOfReferenceDate() {
        LocalDate referenceDate = LocalDate.parse("19/06/2019", DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        ExistingAccount fakeAccount = new ExistingAccount();
        fakeAccount.setMonthStartingAt(22);

        LocalDate result = this.balanceService.calculatePeriodEndDate(fakeAccount, referenceDate);

        assertThat(result.getMonthValue())
            .isEqualTo(referenceDate.getMonthValue());
    }
}
