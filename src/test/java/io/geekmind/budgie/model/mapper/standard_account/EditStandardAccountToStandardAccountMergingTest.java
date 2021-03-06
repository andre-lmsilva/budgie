package io.geekmind.budgie.model.mapper.standard_account;

import io.geekmind.budgie.fixture.StandardAccountFixture;
import io.geekmind.budgie.fixture.EditAccountFixture;
import io.geekmind.budgie.model.dto.standard_account.EditStandardAccount;
import io.geekmind.budgie.model.entity.Account;
import io.geekmind.budgie.model.entity.StandardAccount;
import io.geekmind.budgie.model.mapper.StandardAccountMappingSettings;
import io.geekmind.budgie.model.mapper.DefaultCurrencyMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EditStandardAccountToStandardAccountMergingTest {

    private EditStandardAccount sourceNewAccount;
    private StandardAccount sourceAccount;
    private StandardAccount resultAccount;

    @Before
    public void setUp() {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        new StandardAccountMappingSettings(new DefaultCurrencyMapper()).configure(mapperFactory);
        this.sourceNewAccount = EditAccountFixture.savingsAccount();
        this.sourceAccount = StandardAccountFixture.creditCard();
        this.resultAccount = StandardAccountFixture.creditCard();
        mapperFactory.getMapperFacade().map(this.sourceNewAccount, this.resultAccount);
    }

    @Test
    public void idAttribute_isNotUpdated() {
        assertThat(resultAccount)
            .hasFieldOrPropertyWithValue("id", this.sourceAccount.getId());
    }

    @Test
    public void mainAccountAttribute_isNotUpdated() {
        assertThat(resultAccount)
            .hasFieldOrPropertyWithValue("mainAccount", this.sourceAccount.getMainAccount());
    }

    @Test
    public void nameAttribute_isUpdated() {
        assertThat(resultAccount)
            .hasFieldOrPropertyWithValue("name", this.sourceNewAccount.getName());
    }

    @Test
    public void descriptionAttribute_isUpdated() {
        assertThat(resultAccount)
            .hasFieldOrPropertyWithValue("description", this.sourceNewAccount.getDescription());
    }

    @Test
    public void monthStartingAtAttribute_isUpdated() {
        assertThat(resultAccount)
            .hasFieldOrPropertyWithValue("monthStartingAt", this.sourceNewAccount.getMonthStartingAt());
    }

    @Test
    public void monthBillingDayAtAttribute_isUpdated() {
        assertThat(resultAccount)
            .hasFieldOrPropertyWithValue("monthBillingDayAt", this.sourceNewAccount.getMonthBillingDayAt());
    }

    @Test
    public void showBalanceOnMainAccountAttribute_isUpdated() {
        assertThat(resultAccount)
            .hasFieldOrPropertyWithValue("showBalanceOnParentAccount", this.sourceNewAccount.getShowBalanceOnParentAccount());
    }

    @Test
    public void currencyCodeAttribute_isUpdated() {
        assertThat(resultAccount)
            .hasFieldOrPropertyWithValue("currencyCode", this.sourceNewAccount.getCurrencyCode());
    }

    @Test
    public void parentIdAttribute_isNotUpdated() {
        assertThat(resultAccount)
            .hasFieldOrPropertyWithValue("parent.id", this.sourceAccount.getParent().getId());
    }

}
