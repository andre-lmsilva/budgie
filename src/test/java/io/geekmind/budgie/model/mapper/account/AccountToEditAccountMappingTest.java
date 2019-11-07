package io.geekmind.budgie.model.mapper.account;

import io.geekmind.budgie.fixture.AccountFixture;
import io.geekmind.budgie.model.dto.account.EditAccount;
import io.geekmind.budgie.model.entity.Account;
import io.geekmind.budgie.model.mapper.AccountMappingSettings;
import io.geekmind.budgie.model.mapper.DefaultCurrencyMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountToEditAccountMappingTest {

    private Account sourceAccount;
    private EditAccount resultEditAccount;

    @Before
    public void setUp() {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        new AccountMappingSettings(new DefaultCurrencyMapper()).configure(mapperFactory);

        this.sourceAccount = AccountFixture.creditCard();
        this.resultEditAccount = mapperFactory.getMapperFacade().map(this.sourceAccount, EditAccount.class);
    }

    @Test
    public void idAttribute_isMapped() {
        assertThat(this.resultEditAccount)
            .hasFieldOrPropertyWithValue("id", this.sourceAccount.getId());
    }

    @Test
    public void nameAttribute_isMapped() {
        assertThat(this.resultEditAccount)
            .hasFieldOrPropertyWithValue("name", this.sourceAccount.getName());
    }

    @Test
    public void descriptionAttribute_isMapped() {
        assertThat(this.resultEditAccount)
            .hasFieldOrPropertyWithValue("description", this.sourceAccount.getDescription());
    }

    @Test
    public void monthStartingAtAttribute_isMapped() {
        assertThat(this.resultEditAccount)
            .hasFieldOrPropertyWithValue("monthStartingAt", this.sourceAccount.getMonthStartingAt());
    }

    @Test
    public void monthBillingAtAttribute_isMapped() {
        assertThat(this.resultEditAccount)
            .hasFieldOrPropertyWithValue("monthBillingDayAt", this.sourceAccount.getMonthBillingDayAt());
    }

    @Test
    public void showBalanceOnMainAccountAttribute_isMapped() {
        assertThat(this.resultEditAccount)
            .hasFieldOrPropertyWithValue("showBalanceOnMainAccount", this.sourceAccount.getShowBalanceOnMainAccount());
    }

    @Test
    public void currencyCodeAttribute_isMapped() {
        assertThat(this.resultEditAccount)
            .hasFieldOrPropertyWithValue("currencyCode", this.sourceAccount.getCurrencyCode());
    }

    @Test
    public void parentIdAttribute_isMapped() {
        assertThat(this.resultEditAccount)
            .hasFieldOrPropertyWithValue("parentId", this.sourceAccount.getParent().getId());
    }

}
