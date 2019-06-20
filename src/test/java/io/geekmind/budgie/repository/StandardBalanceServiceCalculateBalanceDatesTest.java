package io.geekmind.budgie.repository;

import io.geekmind.budgie.model.dto.BalanceDates;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class StandardBalanceServiceCalculateBalanceDatesTest {

    @InjectMocks
    @Spy
    private StandardBalanceService balanceService;

    private LocalDate expectedEndDate;

    private LocalDate expectedStartDate;

    private LocalDate expectedBillingDate;

    private LocalDate expectedReferenceDate;

    private LocalDate expectedPreviousPeriodStartDate;

    private LocalDate expectedNextPeriodStartDate;

    private BalanceDates balanceDates;

    @Before
    public void setUp() {
        this.expectedEndDate = LocalDate.parse("31/01/2019", DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        this.expectedStartDate = LocalDate.parse("01/01/2019", DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        this.expectedBillingDate = LocalDate.parse("02/02/2019", DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        this.expectedReferenceDate = LocalDate.parse("19/01/2019", DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        this.expectedPreviousPeriodStartDate = expectedReferenceDate.minusMonths(1L);
        this.expectedNextPeriodStartDate = expectedReferenceDate.plusMonths(1L);

        doReturn(expectedEndDate).when(this.balanceService).calculatePeriodEndDate(any(), eq(this.expectedReferenceDate));
        doReturn(expectedStartDate).when(this.balanceService).calculatePeriodStartDate(any());
        doReturn(expectedBillingDate).when(this.balanceService).calculatePeriodBillingDate(any(), any());

        this.balanceDates = this.balanceService.calculateBalanceDates(null, this.expectedReferenceDate);
    }

    @Test
    public void periodStartDateIsFirstOfJanuary() {
        assertThat(this.balanceDates.getPeriodStartDate()).isEqualTo(this.expectedStartDate);
    }

    @Test
    public void periodEndDateIsEndOfJanuary() {
        assertThat(this.balanceDates.getPeriodEndDate()).isEqualTo(this.expectedEndDate);
    }

    @Test
    public void referenceDateIsTheUsedReferenceDate() {
        assertThat(this.balanceDates.getReferenceDate()).isEqualTo(this.expectedReferenceDate);
    }

    @Test
    public void billingDateIsSecondOfFebruary() {
        assertThat(this.balanceDates.getPeriodBillingDate()).isEqualTo(this.expectedBillingDate);
    }

    @Test
    public void previousPeriodStartDateIsNineteenthOfDecember() {
        assertThat(this.balanceDates.getPreviousPeriodStartDate()).isEqualTo(this.expectedPreviousPeriodStartDate);
    }

    @Test
    public void nextPeriodStartDateIsNineteenthOfFebruary() {
        assertThat(this.balanceDates.getNextPeriodStartDate()).isEqualTo(this.expectedNextPeriodStartDate);
    }
}
