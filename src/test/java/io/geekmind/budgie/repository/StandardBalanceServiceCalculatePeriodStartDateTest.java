package io.geekmind.budgie.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class StandardBalanceServiceCalculatePeriodStartDateTest {

    @InjectMocks
    private StandardBalanceService balanceService;

    @Test
    public void calculatePeriodStartDate_WithDateNotCoincidingWithEndOfMonth_ReturnsDateWithOneMonthBeforeAndOneDayAfter() {
        LocalDate fakeEndDate = LocalDate.parse("18/07/2019", DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        LocalDate result = this.balanceService.calculatePeriodStartDate(fakeEndDate);

        assertThat(result.getYear()).isEqualTo(fakeEndDate.getYear());
        assertThat(result.getMonth()).isEqualTo(Month.JUNE);
        assertThat(result.getDayOfMonth()).isEqualTo(19);
    }

    @Test
    public void calculatePeriodStartDate_WithDateInJanuary_ReturnsDateOfPreviousYear() {
        LocalDate fakeEndDate = LocalDate.parse("18/01/2019", DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        LocalDate result = this.balanceService.calculatePeriodStartDate(fakeEndDate);

        assertThat(result.getYear()).isEqualTo(fakeEndDate.getYear()-1);
        assertThat(result.getMonth()).isEqualTo(Month.DECEMBER);
        assertThat(result.getDayOfMonth()).isEqualTo(19);
    }

     @Test
    public void calculatePeriodStartDate_WithEndOfMonthAsDate_ReturnsFirstDayOfSameMonth() {
        LocalDate fakeEndDate = LocalDate.parse("28/02/2019", DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        LocalDate result = this.balanceService.calculatePeriodStartDate(fakeEndDate);

        assertThat(result.getYear()).isEqualTo(fakeEndDate.getYear());
        assertThat(result.getMonth()).isEqualTo(Month.FEBRUARY);
        assertThat(result.getDayOfMonth()).isEqualTo(1);
    }
}
