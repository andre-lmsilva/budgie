package io.geekmind.budgie.repository;

import io.geekmind.budgie.model.dto.BalanceSummary;
import io.geekmind.budgie.model.dto.ExistingRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class StandardBalanceServiceCalculateBalanceSummaryTest {

    @InjectMocks
    @Spy
    private StandardBalanceService balanceService;

    private BalanceSummary balanceSummary;

    @Before
    public void setUp() {
        ExistingRecord today = new ExistingRecord();
        today.setRecordDate(LocalDate.now());
        today.setRecordValue(BigDecimal.valueOf(100.00D));

        ExistingRecord tomorrow = new ExistingRecord();
        tomorrow.setRecordDate(LocalDate.now().plusDays(1L));
        tomorrow.setRecordValue(BigDecimal.valueOf(-10.5D));

        this.balanceSummary = this.balanceService.calculateBalanceSummary(
            Arrays.asList(today, tomorrow),
            LocalDate.now(),
            LocalDate.now().plusDays(1L)
        );
    }

    @Test
    public void totalIncomesEqualsToOneHundred() {
        assertThat(this.balanceSummary.getTotalIncomes())
            .isEqualByComparingTo(BigDecimal.valueOf(100D));
    }

    @Test
    public void totalExpensesIsEqualToTenAndFifthCents() {
         assertThat(this.balanceSummary.getTotalExpenses())
            .isEqualByComparingTo(BigDecimal.valueOf(-10.5D));
    }

    @Test
    public void balanceUpToDateIsEqualsToOneHundred() {
         assertThat(this.balanceSummary.getBalanceUpToDate())
            .isEqualByComparingTo(BigDecimal.valueOf(100D));
    }

    @Test
    public void finalBalanceIsEqualToEighthNineAndFifthCents() {
        assertThat(this.balanceSummary.getFinalBalance())
            .isEqualByComparingTo(BigDecimal.valueOf(89.5D));
    }
}
