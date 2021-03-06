package io.geekmind.budgie.model.mapper.standard_account;

import io.geekmind.budgie.fixture.NewAccountFixture;
import io.geekmind.budgie.model.dto.standard_account.NewStandardAccount;
import io.geekmind.budgie.model.entity.StandardAccount;
import io.geekmind.budgie.model.mapper.StandardAccountMappingSettings;
import io.geekmind.budgie.model.mapper.DefaultCurrencyMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class NewStandardAccountToStandardAccountMappingTest {

    private NewStandardAccount sourceNewAccount;
    private StandardAccount resultEntity;

    @Before
    public void setUp() {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        new StandardAccountMappingSettings(new DefaultCurrencyMapper()).configure(mapperFactory);
        this.sourceNewAccount = NewAccountFixture.getMainAccount();
        this.resultEntity = mapperFactory.getMapperFacade().map(this.sourceNewAccount, StandardAccount.class);
    }

    @Test
    public void idAttribute_isNotMapped() {
        assertThat(this.resultEntity.getId()).isNull();
    }

    @Test
    public void mainAccountAttribute_isFalseByDefault() {
        assertThat(this.resultEntity)
            .hasFieldOrPropertyWithValue("mainAccount", Boolean.FALSE);
    }

    @Test
    public void nameAttribute_isMapped() {
        assertThat(this.resultEntity)
            .hasFieldOrPropertyWithValue("name", this.sourceNewAccount.getName());
    }

    @Test
    public void descriptionAttribute_isMapped() {
        assertThat(this.resultEntity)
            .hasFieldOrPropertyWithValue("description", this.sourceNewAccount.getDescription());
    }

    @Test
    public void monthStartingAtAttribute_isMapped() {
        assertThat(this.resultEntity)
            .hasFieldOrPropertyWithValue("monthStartingAt", this.sourceNewAccount.getMonthStartingAt());
    }

    @Test
    public void monthBillingDayAtAttribute_isMapped() {
        assertThat(this.resultEntity)
            .hasFieldOrPropertyWithValue("monthBillingDayAt", this.sourceNewAccount.getMonthBillingDayAt());
    }

    @Test
    public void showBalanceOnMainAccountAttribute_isMapped() {
        assertThat(this.resultEntity)
            .hasFieldOrPropertyWithValue("showBalanceOnParentAccount", this.sourceNewAccount.getShowBalanceOnParentAccount());
    }

    @Test
    public void currencyCodeAttributeAttribute_isMapped() {
        assertThat(this.resultEntity)
            .hasFieldOrPropertyWithValue("currencyCode", this.sourceNewAccount.getCurrencyCode());
    }

    @Test
    public void parentIdAttribute_isMappedToParentIdAttribute() {
        assertThat(this.resultEntity)
            .hasFieldOrPropertyWithValue("parent.id", this.sourceNewAccount.getParentId());
    }

}
