package io.geekmind.budgie.model.mapper.project_account;

import io.geekmind.budgie.fixture.ExistingStandardAccountFixture;
import io.geekmind.budgie.model.dto.standard_account.ExistingStandardAccount;
import io.geekmind.budgie.model.entity.ProjectAccount;
import io.geekmind.budgie.model.mapper.ProjectAccountMappingSettings;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ExistingStandardAccountToProjectAccountMappingTest {

    private ExistingStandardAccount sourceStandardAccount;
    private ProjectAccount resultProjectAccount;

    @Before
    public void setUp() {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        new ProjectAccountMappingSettings(null).configure(mapperFactory);
        this.sourceStandardAccount = ExistingStandardAccountFixture.getMainAccount();
        this.resultProjectAccount = mapperFactory.getMapperFacade().map(this.sourceStandardAccount, ProjectAccount.class);
    }

    @Test
    public void idAttribute_isNotMapped() {
        assertThat(this.resultProjectAccount)
            .hasFieldOrPropertyWithValue("id", null);
    }

    @Test
    public void nameAttribute_isNotMapped() {
        assertThat(this.resultProjectAccount)
            .hasFieldOrPropertyWithValue("name", null);
    }

    @Test
    public void descriptionAttribute_isNotMapped() {
        assertThat(this.resultProjectAccount)
            .hasFieldOrPropertyWithValue("description", null);
    }

    @Test
    public void monthStartingAtAttribute_isMapped() {
        assertThat(this.resultProjectAccount)
            .hasFieldOrPropertyWithValue("monthStartingAt", this.sourceStandardAccount.getMonthStartingAt());
    }

    @Test
    public void monthBillingDayAtAttribute_isMapped() {
        assertThat(this.resultProjectAccount)
            .hasFieldOrPropertyWithValue("monthBillingDayAt", this.sourceStandardAccount.getMonthBillingDayAt());
    }

    @Test
    public void currencyCodeAttribute_isMapped() {
        assertThat(this.resultProjectAccount)
            .hasFieldOrPropertyWithValue("currencyCode", this.sourceStandardAccount.getCurrency().getCurrencyCode());
    }

    @Test
    public void parentAttribute_isNotMapped() {
        assertThat(this.resultProjectAccount)
            .hasFieldOrPropertyWithValue("parent", null);
    }

    @Test
    public void showBalanceOnParentAccountAttribute_isSetTrue() {
        assertThat(this.resultProjectAccount)
            .hasFieldOrPropertyWithValue("showBalanceOnParentAccount", true);
    }

}
