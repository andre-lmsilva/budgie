package io.geekmind.budgie.model.mapper.account;

import io.geekmind.budgie.fixture.ExistingAccountFixture;
import io.geekmind.budgie.model.dto.account.ExistingAccount;
import io.geekmind.budgie.model.entity.Account;
import io.geekmind.budgie.model.mapper.AccountMappingSettings;
import io.geekmind.budgie.model.mapper.DefaultCurrencyMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ExistingAccountToAccountMappingTest {

    private ExistingAccount sourceExistingAccount;
    private Account resultAccount;

    @Before
    public void setUp() {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        new AccountMappingSettings(new DefaultCurrencyMapper()).configure(mapperFactory);
        this.sourceExistingAccount = ExistingAccountFixture.getMainAccount();
        this.resultAccount = mapperFactory.getMapperFacade().map(this.sourceExistingAccount, Account.class);
    }

    @Test
    public void allAttributes_isNotMapped() {
        assertThat(this.resultAccount)
            .hasFieldOrPropertyWithValue("id", null)
            .hasFieldOrPropertyWithValue("name", null)
            .hasFieldOrPropertyWithValue("description", null)
            .hasFieldOrPropertyWithValue("monthStartingAt", null)
            .hasFieldOrPropertyWithValue("monthBillingDayAt", null)
            .hasFieldOrPropertyWithValue("showBalanceOnMainAccount", null)
            .hasFieldOrPropertyWithValue("mainAccount", Boolean.FALSE)
            .hasFieldOrPropertyWithValue("currencyCode", null);
    }

}
