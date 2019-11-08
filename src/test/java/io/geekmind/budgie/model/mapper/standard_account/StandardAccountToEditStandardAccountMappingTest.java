package io.geekmind.budgie.model.mapper.standard_account;

import io.geekmind.budgie.fixture.StandardAccountFixture;
import io.geekmind.budgie.model.dto.standard_account.EditStandardAccount;
import io.geekmind.budgie.model.entity.StandardAccount;
import io.geekmind.budgie.model.mapper.DefaultCurrencyMapper;
import io.geekmind.budgie.model.mapper.StandardAccountMappingSettings;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StandardAccountToEditStandardAccountMappingTest {

    private StandardAccount sourceAccount;
    private EditStandardAccount resultEditAccount;

    @Before
    public void setUp() {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        new StandardAccountMappingSettings(new DefaultCurrencyMapper()).configure(mapperFactory);

        this.sourceAccount = StandardAccountFixture.creditCard();
        this.resultEditAccount = mapperFactory.getMapperFacade().map(this.sourceAccount, EditStandardAccount.class);
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
            .hasFieldOrPropertyWithValue("showBalanceOnParentAccount", this.sourceAccount.getShowBalanceOnParentAccount());
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
