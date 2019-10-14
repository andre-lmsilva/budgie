package io.geekmind.budgie.repository;

import io.geekmind.budgie.balance.BaseBalanceCalculationStep;
import io.geekmind.budgie.balance.commons.BalanceDatesCalculator;
import io.geekmind.budgie.fixture.BalanceFixture;
import io.geekmind.budgie.fixture.ExistingAccountFixture;
import io.geekmind.budgie.model.dto.Balance;
import io.geekmind.budgie.model.dto.BalanceCalculationRequest;
import io.geekmind.budgie.model.dto.ExistingAccount;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(MockitoJUnitRunner.class)
public class StandardBalanceServiceTest {

    @Mock
    private AccountService accountService;

    @Mock
    private BaseBalanceCalculationStep calculationsChainEntryStep;

    @Mock
    private BalanceDatesCalculator balanceDatesCalculator;

    @Spy
    @InjectMocks
    private StandardBalanceService service;

    @Test
    public void retrieveAccount_withAccountIdAsNull_RetrievesMainAccount() {
        ExistingAccount fakeMainAccount = ExistingAccountFixture.getMainAccount();
        doReturn(Optional.of(fakeMainAccount))
            .when(this.accountService).getMainAccount();

        Optional<ExistingAccount> result = this.service.retrieveAccount(null);

        assertThat(result).isNotNull().isNotEmpty().containsSame(fakeMainAccount);
        verify(this.accountService, Mockito.never()).loadById(Mockito.anyInt());
    }

    @Test
    public void retrieveAccount_withExistingAccountId_RetrievesAccount() {
        ExistingAccount fakeAccount = ExistingAccountFixture.getSavingsAccount();
        doReturn(Optional.of(fakeAccount))
            .when(this.accountService).loadById(eq(-1));

        Optional<ExistingAccount> result = this.service.retrieveAccount(-1);

        assertThat(result).isNotNull().isNotEmpty().containsSame(fakeAccount);
        verify(this.accountService, Mockito.never()).getMainAccount();
    }

    @Test
    public void retrieveAccount_withNonExistingAccountId_ReturnsEmptyOptional() {
        doReturn(Optional.empty())
            .when(this.accountService).loadById(eq(0));

        Optional<ExistingAccount> result = this.service.retrieveAccount(0);

        assertThat(result).isNotNull().isEmpty();
        verify(this.accountService, Mockito.never()).getMainAccount();
    }

    @Test
    public void calculateDependantAccountsBalancesFor_withNoDependantAccount_ReturnsEmptyList() {
        ExistingAccount fakeMainAccount = ExistingAccountFixture.getMainAccount();
        LocalDate fakeReferenceDate = LocalDate.now();
        doReturn(Collections.emptyList())
            .when(this.accountService).loadDependantAccounts();

        LocalDate fakePeriodEndDate = LocalDate.now();
        doReturn(fakePeriodEndDate)
            .when(this.balanceDatesCalculator).calculatePeriodEndDate(eq(fakeReferenceDate), eq(fakeMainAccount));

        List<Balance> result = this.service.calculateDependantAccountsBalancesFor(fakeMainAccount, fakeReferenceDate);

        assertThat(result).isNotNull().isEmpty();
        verify(this.service, never()).generateBalance(anyInt(), any());
    }

    @Test
    public void calculateDependantAccountsBalanceFor_withDependantAccount_ReturnsNonEmptyList() {
        ExistingAccount fakeMainAccount = ExistingAccountFixture.getMainAccount();
        ExistingAccount fakeSavingsAccount = ExistingAccountFixture.getSavingsAccount();
        LocalDate fakeReferenceDate = LocalDate.now();
        doReturn(Collections.singletonList(fakeSavingsAccount))
            .when(this.accountService).loadDependantAccounts();

        LocalDate fakePeriodEndDate = LocalDate.now();
        doReturn(fakePeriodEndDate)
            .when(this.balanceDatesCalculator).calculatePeriodEndDate(eq(fakeReferenceDate), eq(fakeMainAccount));

        LocalDate fakeAccountPeriodEndDate = LocalDate.now();
        doReturn(fakeAccountPeriodEndDate)
            .when(this.balanceDatesCalculator).calculatePeriodEndDateBasedOnBillingDate(any(), eq(fakeSavingsAccount));

        Balance fakeBalance = BalanceFixture.getCurrentPeriodBalance();
        doReturn(fakeBalance)
            .when(this.service).generateBalance(eq(fakeSavingsAccount.getId()), eq(fakeAccountPeriodEndDate));

        List<Balance> result = this.service.calculateDependantAccountsBalancesFor(fakeMainAccount, fakeAccountPeriodEndDate);

        assertThat(result).containsOnly(fakeBalance);
    }

    @Test
    public void generateBalance_withAccountNotFound_ReturnsEmptyBalance() {
        doReturn(Optional.empty())
            .when(this.service).retrieveAccount(eq(-1));

        Balance result = this.service.generateBalance(-1, null);

        assertThat(result).isNotNull();
        verifyZeroInteractions(this.calculationsChainEntryStep);
    }

    @Test
    public void generateBalance_withNonMainAccount_CalculatesAccountBalance() {
        doReturn(Optional.of(ExistingAccountFixture.getSavingsAccount()))
            .when(this.service).retrieveAccount(eq(-1));

        Balance fakeBalance = BalanceFixture.getCurrentPeriodBalance();
        doAnswer(invocationOnMock -> {
            BalanceCalculationRequest request = invocationOnMock.getArgument(0);
            assertThat(request.getBalance().getBalanceDates().getReferenceDate()).isEqualTo(LocalDate.now());
            request.setBalance(fakeBalance);
            return invocationOnMock;
        }).when(this.calculationsChainEntryStep).proceed(any(BalanceCalculationRequest.class));

        Balance result = this.service.generateBalance(-1, null);

        assertThat(result).isSameAs(fakeBalance);
        verify(this.service, never()).calculateDependantAccountsBalancesFor(any(), any());
    }

    @Test
    public void generateBalance_withMainAccount_CalculatesDependantAccountBalance() {
        ExistingAccount fakeAccount = ExistingAccountFixture.getMainAccount();
        doReturn(Optional.of(fakeAccount))
            .when(this.service).retrieveAccount(eq(-1));
        doReturn(Collections.emptyList())
            .when(this.service).calculateDependantAccountsBalancesFor(eq(fakeAccount), eq(LocalDate.now()));

        Balance fakeBalance = BalanceFixture.getCurrentPeriodBalance();
        doAnswer(invocationOnMock -> {
            BalanceCalculationRequest request = invocationOnMock.getArgument(0);

            assertThat(request.getBalance().getBalanceDates().getReferenceDate()).isEqualTo(LocalDate.now());
            assertThat(request.getBalance().getAccount()).isSameAs(fakeAccount);
            request.setBalance(fakeBalance);

            return invocationOnMock;
        }).when(this.calculationsChainEntryStep).proceed(any(BalanceCalculationRequest.class));

        Balance result = this.service.generateBalance(-1, null);

        assertThat(result).isSameAs(fakeBalance);
        verify(this.service).calculateDependantAccountsBalancesFor(eq(fakeAccount), eq(LocalDate.now()));
    }

}