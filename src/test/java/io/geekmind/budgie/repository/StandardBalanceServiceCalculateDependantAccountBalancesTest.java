package io.geekmind.budgie.repository;

import io.geekmind.budgie.fixture.BalanceDatesFixture;
import io.geekmind.budgie.fixture.BalanceFixture;
import io.geekmind.budgie.fixture.ExistingAccountFixture;
import io.geekmind.budgie.fixture.ExistingRecordFixture;
import io.geekmind.budgie.model.dto.*;
import ma.glasnost.orika.MapperFacade;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class StandardBalanceServiceCalculateDependantAccountBalancesTest {

    @Mock
    private AccountService accountService;

    @Mock
    private MapperFacade mapper;

    @Spy
    @InjectMocks
    private StandardBalanceService balanceService;

    private ExistingAccount fakeSavingsAccount;
    private BalanceDates fakeBalanceDates;
    private LocalDate expectedReferenceDate;
    private ExistingRecord fakeDependantAccountRecord;
    private Balance fakeDependantAccountBalance;
    private List<ExistingRecord> result;

    @Before
    public void setUp() {
        this.fakeBalanceDates = BalanceDatesFixture.getBalanceDatesForCurrentPeriod();
        this.expectedReferenceDate = this.fakeBalanceDates.getReferenceDate().minusMonths(1L);

        this.fakeSavingsAccount = ExistingAccountFixture.getSavingsAccount();
        doReturn(Collections.singletonList(this.fakeSavingsAccount))
            .when(this.accountService).loadDependantAccounts();

        this.fakeDependantAccountBalance = BalanceFixture.getCurrentPeriodBalance();
        doReturn(this.expectedReferenceDate)
            .when(this.balanceService).calculatePeriodEndDateBasedOnBillingDate(any(), any());
        doReturn(this.fakeDependantAccountBalance)
            .when(this.balanceService).generateBalance(eq(this.fakeSavingsAccount.getId()), eq(this.expectedReferenceDate));

        this.fakeDependantAccountRecord = ExistingRecordFixture.getWithValue(BigDecimal.TEN);
        doReturn(fakeDependantAccountRecord)
            .when(this.mapper).map(eq(this.fakeDependantAccountBalance), eq(DependantAccountRecord.class));

        this.result = this.balanceService.calculateDependantAccountBalances(this.fakeBalanceDates);
    }

    @Test
    public void loadsAllDependantAccountsFromDatabase() {
        verify(this.accountService).loadDependantAccounts();
    }

    @Test
    public void generateTheBalanceForTheAccountUsingTheExpectedReferenceDate() {
        verify(this.balanceService)
            .generateBalance(eq(this.fakeSavingsAccount.getId()), eq(expectedReferenceDate));
    }

    @Test
    public void mapTheBalanceToAnExistingRecordInstance() {
        verify(this.mapper)
            .map(eq(this.fakeDependantAccountBalance), eq(DependantAccountRecord.class));
    }

    @Test
    public void returnsMappedExistingRecords() {
        assertThat(this.result).containsOnly(this.fakeDependantAccountRecord);
    }

}
