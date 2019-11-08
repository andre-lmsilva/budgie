package io.geekmind.budgie.model.mapper.standard_account;

import io.geekmind.budgie.fixture.StandardAccountFixture;
import io.geekmind.budgie.model.dto.standard_account.NewStandardAccount;
import io.geekmind.budgie.model.entity.StandardAccount;
import io.geekmind.budgie.model.mapper.DefaultCurrencyMapper;
import io.geekmind.budgie.model.mapper.StandardAccountMappingSettings;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class StandardAccountToNewStandardAccountMappingTest {

    private StandardAccount sourceAccount;
    private NewStandardAccount resultNewAccount;

    @Before
    public void setUp() {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        new StandardAccountMappingSettings(new DefaultCurrencyMapper()).configure(mapperFactory);
        this.sourceAccount = StandardAccountFixture.getMainAccount();
        this.resultNewAccount = mapperFactory.getMapperFacade().map(this.sourceAccount, NewStandardAccount.class);
    }

    @Test
    public void allAttributes_isNotMapped() {
        assertThat(this.resultNewAccount)
            .hasFieldOrPropertyWithValue("name", null)
            .hasFieldOrPropertyWithValue("description", null)
            .hasFieldOrPropertyWithValue("monthStartingAt", null)
            .hasFieldOrPropertyWithValue("monthBillingDayAt", null)
            .hasFieldOrPropertyWithValue("showBalanceOnParentAccount", null)
            .hasFieldOrPropertyWithValue("currencyCode", null);
    }

}
