package io.geekmind.budgie.model.mapper.account;

import io.geekmind.budgie.fixture.AccountFixture;
import io.geekmind.budgie.fixture.NewAccountFixture;
import io.geekmind.budgie.model.dto.account.NewAccount;
import io.geekmind.budgie.model.entity.Account;
import io.geekmind.budgie.model.mapper.AccountMappingSettings;
import io.geekmind.budgie.model.mapper.DefaultCurrencyMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EditAccountToAccountMergingTest {

    private NewAccount sourceNewAccount;
    private Account sourceAccount;
    private Account resultAccount;

    @Before
    public void setUp() {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        new AccountMappingSettings(new DefaultCurrencyMapper()).configure(mapperFactory);
        this.sourceNewAccount = NewAccountFixture.getMainAccount();
        this.sourceAccount = AccountFixture.getMainAccount();
        this.resultAccount = AccountFixture.getMainAccount();
        mapperFactory.getMapperFacade().map(this.sourceNewAccount, this.resultAccount);
    }

    @Test
    public void idAttribute_isNotUpdated() {
        assertThat(resultAccount)
            .hasFieldOrPropertyWithValue("id", this.sourceAccount.getId());
        assertThat(resultAccount.getId())
            .isNotEqualTo(this.sourceNewAccount.getId());
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
            .hasFieldOrPropertyWithValue("showBalanceOnMainAccount", this.sourceNewAccount.getShowBalanceOnMainAccount());
    }

    @Test
    public void currencyCodeAttribute_isUpdated() {
        assertThat(resultAccount)
            .hasFieldOrPropertyWithValue("currencyCode", this.sourceNewAccount.getCurrencyCode());
    }

}
