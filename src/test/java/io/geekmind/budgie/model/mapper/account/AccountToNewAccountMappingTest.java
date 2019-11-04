package io.geekmind.budgie.model.mapper.account;

import io.geekmind.budgie.fixture.AccountFixture;
import io.geekmind.budgie.model.dto.account.NewAccount;
import io.geekmind.budgie.model.entity.Account;
import io.geekmind.budgie.model.mapper.AccountMappingSettings;
import io.geekmind.budgie.model.mapper.DefaultCurrencyMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class AccountToNewAccountMappingTest {

    private Account sourceAccount;
    private NewAccount resultNewAccount;

    @Before
    public void setUp() {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        new AccountMappingSettings(new DefaultCurrencyMapper()).configure(mapperFactory);
        this.sourceAccount = AccountFixture.getMainAccount();
        this.resultNewAccount = mapperFactory.getMapperFacade().map(this.sourceAccount, NewAccount.class);
    }

    @Test
    public void allAttributes_isNotMapped() {
        assertThat(this.resultNewAccount)
            .hasFieldOrPropertyWithValue("id", null)
            .hasFieldOrPropertyWithValue("name", null)
            .hasFieldOrPropertyWithValue("description", null)
            .hasFieldOrPropertyWithValue("monthStartingAt", null)
            .hasFieldOrPropertyWithValue("monthBillingDayAt", null)
            .hasFieldOrPropertyWithValue("showBalanceOnMainAccount", null)
            .hasFieldOrPropertyWithValue("currencyCode", null);
    }

}
