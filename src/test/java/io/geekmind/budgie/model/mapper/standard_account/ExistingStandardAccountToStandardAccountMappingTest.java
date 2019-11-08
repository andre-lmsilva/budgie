package io.geekmind.budgie.model.mapper.standard_account;

import io.geekmind.budgie.fixture.ExistingAccountFixture;
import io.geekmind.budgie.model.dto.standard_account.ExistingStandardAccount;
import io.geekmind.budgie.model.entity.Account;
import io.geekmind.budgie.model.mapper.StandardAccountMappingSettings;
import io.geekmind.budgie.model.mapper.DefaultCurrencyMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ExistingStandardAccountToStandardAccountMappingTest {

    private ExistingStandardAccount sourceExistingAccount;
    private Account resultAccount;

    @Before
    public void setUp() {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        new StandardAccountMappingSettings(new DefaultCurrencyMapper()).configure(mapperFactory);
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
            .hasFieldOrPropertyWithValue("showBalanceOnParentAccount", null)
            .hasFieldOrPropertyWithValue("mainAccount", Boolean.FALSE)
            .hasFieldOrPropertyWithValue("currencyCode", null)
            .hasFieldOrPropertyWithValue("parent", null)
            .hasFieldOrPropertyWithValue("dependants", null);
    }

}
