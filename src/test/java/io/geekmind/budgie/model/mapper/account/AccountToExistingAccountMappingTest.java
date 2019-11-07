package io.geekmind.budgie.model.mapper.account;

import io.geekmind.budgie.fixture.AccountCurrencyFixture;
import io.geekmind.budgie.fixture.AccountFixture;
import io.geekmind.budgie.model.dto.AccountCurrency;
import io.geekmind.budgie.model.dto.account.ExistingAccount;
import io.geekmind.budgie.model.entity.Account;
import io.geekmind.budgie.model.mapper.AccountMappingSettings;
import io.geekmind.budgie.model.mapper.CurrencyMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class AccountToExistingAccountMappingTest {

    private Account sourceAccount;
    private AccountCurrency resultAccountCurrency;
    private ExistingAccount resultExistingAccount;

    @Before
    public void setUp() {
        CurrencyMapper mockCurrencyMapper = mock(CurrencyMapper.class);
        this.resultAccountCurrency = AccountCurrencyFixture.euro();
        doReturn(this.resultAccountCurrency)
            .when(mockCurrencyMapper).mapFrom(any());

        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        new AccountMappingSettings(mockCurrencyMapper).configure(mapperFactory);

        this.sourceAccount = AccountFixture.creditCard();
        this.resultExistingAccount = mapperFactory.getMapperFacade().map(this.sourceAccount, ExistingAccount.class);
    }

    @Test
    public void idAttribute_isMapped() {
        assertThat(this.resultExistingAccount)
            .hasFieldOrPropertyWithValue("id", sourceAccount.getId());
    }

    @Test
    public void nameAttribute_isMapped() {
        assertThat(this.resultExistingAccount)
            .hasFieldOrPropertyWithValue("name", sourceAccount.getName());
    }

    @Test
    public void descriptionAttribute_isMapped() {
        assertThat(this.resultExistingAccount)
            .hasFieldOrPropertyWithValue("description", sourceAccount.getDescription());
    }

    @Test
    public void monthStartingAtAttribute_isMapped() {
        assertThat(this.resultExistingAccount)
            .hasFieldOrPropertyWithValue("monthStartingAt", sourceAccount.getMonthStartingAt());
    }

    @Test
    public void monthBillingDayAtAttribute_isMapped() {
        assertThat(this.resultExistingAccount)
            .hasFieldOrPropertyWithValue("monthBillingDayAt", sourceAccount.getMonthBillingDayAt());
    }

    @Test
    public void showBalanceOnMainAccountAttribute_isMapped() {
        assertThat(this.resultExistingAccount)
            .hasFieldOrPropertyWithValue("showBalanceOnMainAccount", sourceAccount.getShowBalanceOnMainAccount());
    }

    @Test
    public void mainAccountAttribute_isMapped() {
        assertThat(this.resultExistingAccount)
            .hasFieldOrPropertyWithValue("mainAccount", sourceAccount.getMainAccount());
    }

    @Test
    public void currencyAttribute_isMapped() {
        assertThat(this.resultExistingAccount.getCurrency())
            .isSameAs(this.resultAccountCurrency);
    }

    @Test
    public void parentAttribute_isMapped() {
        assertThat(this.resultExistingAccount.getParent())
            .isNotNull()
            .hasFieldOrPropertyWithValue("id", this.sourceAccount.getParent().getId());
    }

    @Test
    public void dependantsAttribute_isMapped() {
        assertThat(this.resultExistingAccount.getDependants())
            .isNotEmpty().hasSize(1)
            .element(0).hasFieldOrPropertyWithValue("id", this.sourceAccount.getDependants().get(0).getId());
    }

}
