package io.geekmind.budgie.repository;

import io.geekmind.budgie.fixture.BalanceDatesFixture;
import io.geekmind.budgie.fixture.BalanceSummaryFixture;
import io.geekmind.budgie.fixture.ExistingAccountFixture;
import io.geekmind.budgie.fixture.ExistingRecordFixture;
import io.geekmind.budgie.model.dto.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class StandardBalanceServiceGenerateBalanceTest {

    @Spy
    @InjectMocks
    private StandardBalanceService balanceService;

    @Test
    public void generateBalance_WithNonExistingAccountId_ReturnsEmptyBalance() {
        doReturn(Optional.empty())
            .when(this.balanceService).retrieveAccount(eq(-1));

        Balance result = this.balanceService.generateBalance(-1, LocalDate.now());

        assertThat(result).isNotNull();
        verify(this.balanceService, times(0)).calculateBalanceDates(any(), any());
        verify(this.balanceService, times(0)).loadAccountRecords(any(), any());
        verify(this.balanceService, times(0)).calculateBalanceSummary(any(), any(), any());
        verify(this.balanceService, times(0)).calculateCategoryBalanceSummary(any(), any());
    }

    @Test
    public void generateBalance_WithExistingAccountId_ReturnsNonEmptyBalance() {
        ExistingAccount fakeAccount = ExistingAccountFixture.getMainAccount();
        BalanceDates fakeBalanceDates = BalanceDatesFixture.getBalanceDatesForCurrentPeriod();
        List<ExistingRecord> fakeRecords = Collections.singletonList(ExistingRecordFixture.getWithValue(BigDecimal.TEN));
        BalanceSummary fakeSummary = BalanceSummaryFixture.get();
        LocalDate fakeReferenceDate = LocalDate.now();
        List<CategoryBalanceSummary> fakeCategoryBalanceSummary = Collections.emptyList();

        doReturn(Optional.of(fakeAccount))
            .when(this.balanceService).retrieveAccount(eq(-1));
        doReturn(fakeBalanceDates)
            .when(this.balanceService).calculateBalanceDates(eq(fakeAccount), eq(fakeReferenceDate));
        doReturn(fakeRecords)
            .when(this.balanceService).loadAccountRecords(eq(fakeAccount), eq(fakeBalanceDates));
        doReturn(fakeSummary)
            .when(this.balanceService).calculateBalanceSummary(
                eq(fakeRecords),
                eq(fakeBalanceDates.getPeriodStartDate()),
                eq(fakeBalanceDates.getPeriodEndDate()
            )
        );
        doReturn(fakeCategoryBalanceSummary)
            .when(this.balanceService).calculateCategoryBalanceSummary(eq(fakeRecords), eq(fakeSummary));
        doReturn(new ArrayList<>())
            .when(this.balanceService).loadApplicableTemplateRecords(anyInt(), anyList());

        Balance result = this.balanceService.generateBalance(-1, LocalDate.now());

        assertThat(result)
            .hasFieldOrPropertyWithValue("account", fakeAccount)
            .hasFieldOrPropertyWithValue("balanceDates", fakeBalanceDates)
            .hasFieldOrPropertyWithValue("records", fakeRecords)
            .hasFieldOrPropertyWithValue("summary", fakeSummary)
            .hasFieldOrPropertyWithValue("categoryBalanceSummaries", fakeCategoryBalanceSummary);
    }
}
