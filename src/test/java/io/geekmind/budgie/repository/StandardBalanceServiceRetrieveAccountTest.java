package io.geekmind.budgie.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class StandardBalanceServiceRetrieveAccountTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private StandardBalanceService balanceService;

    @Test
    public void retrieveAccount_WithNullIdAsArgument_RetrievesMainAccount() {
        doReturn(Optional.empty()).when(this.accountService).getMainAccount();
        this.balanceService.retrieveAccount(null);
        verify(this.accountService).getMainAccount();
    }

    @Test
    public void retrieveAccount_WithNonNullIdAsArgument_RetrievesDifferentAccount() {
        doReturn(Optional.empty()).when(this.accountService).loadById(eq(-1));
        this.balanceService.retrieveAccount(-1);
        verify(this.accountService).loadById(eq(-1));
    }

}
