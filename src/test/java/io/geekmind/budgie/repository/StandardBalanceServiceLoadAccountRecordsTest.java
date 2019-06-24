package io.geekmind.budgie.repository;

import io.geekmind.budgie.fixture.BalanceDatesFixture;
import io.geekmind.budgie.fixture.ExistingAccountFixture;
import io.geekmind.budgie.fixture.ExistingRecordFixture;
import io.geekmind.budgie.model.dto.BalanceDates;
import io.geekmind.budgie.model.dto.ExistingAccount;
import io.geekmind.budgie.model.dto.ExistingRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class StandardBalanceServiceLoadAccountRecordsTest {

    @Mock(name = "recordService")
    private RecordService recordService;

    @Spy
    @InjectMocks
    private StandardBalanceService balanceService;

    @Test
    public void loadAccountRecords_WithRegularAccount_RetrieveOnlyTheAccountRecords() {
        ExistingRecord fakeRecord = ExistingRecordFixture.getWithValue(BigDecimal.TEN);
        BalanceDates fakeBalanceDates = BalanceDatesFixture.getBalanceDatesForCurrentPeriod();
        ExistingAccount fakeAccount = ExistingAccountFixture.getSavingsAccount();

        doReturn(Collections.singletonList(fakeRecord))
            .when(this.recordService).loadAll(
                eq(fakeAccount.getId()),
                eq(fakeBalanceDates.getPeriodStartDate()),
                eq(fakeBalanceDates.getPeriodEndDate())
        );

        List<ExistingRecord> result = this.balanceService.loadAccountRecords(fakeAccount, fakeBalanceDates);

        assertThat(result).containsOnly(fakeRecord);
        verify(this.balanceService, times(0))
            .calculateDependantAccountBalances(eq(fakeBalanceDates));
    }

    @Test
    public void loadAccountRecord_WithMainAccount_RetrievesDependantAccountRecords() {
        ExistingRecord fakeRecord = ExistingRecordFixture.getWithValue(BigDecimal.TEN);
        ExistingRecord fakeAccountBalanceRecord = ExistingRecordFixture.getWithValue(BigDecimal.ONE);
        fakeAccountBalanceRecord.setRecordDate(fakeRecord.getRecordDate().minusDays(1L));
        BalanceDates fakeBalanceDates = BalanceDatesFixture.getBalanceDatesForCurrentPeriod();
        ExistingAccount fakeAccount = ExistingAccountFixture.getMainAccount();

        List<ExistingRecord> fakeAccountRecordList = new ArrayList<>();
        fakeAccountRecordList.add(fakeRecord);
        doReturn(fakeAccountRecordList)
            .when(this.recordService).loadAll(
                eq(fakeAccount.getId()),
                eq(fakeBalanceDates.getPeriodStartDate()),
                eq(fakeBalanceDates.getPeriodEndDate())
        );

        List<ExistingRecord> fakeAccontBalanceRecordList = new ArrayList<>();
        fakeAccontBalanceRecordList.add(fakeAccountBalanceRecord);
        doReturn(fakeAccontBalanceRecordList)
            .when(this.balanceService).calculateDependantAccountBalances(eq(fakeBalanceDates));

        List<ExistingRecord> result = this.balanceService.loadAccountRecords(fakeAccount, fakeBalanceDates);

        assertThat(result).containsExactly(fakeAccountBalanceRecord, fakeRecord);
        verify(this.balanceService).calculateDependantAccountBalances(eq(fakeBalanceDates));
    }

}
