package io.geekmind.budgie.model.mapper.project_account;

import io.geekmind.budgie.fixture.AccountCurrencyFixture;
import io.geekmind.budgie.fixture.ProjectAccountFixture;
import io.geekmind.budgie.model.dto.account.AccountCurrency;
import io.geekmind.budgie.model.dto.project_account.ExistingProjectAccount;
import io.geekmind.budgie.model.entity.ProjectAccount;
import io.geekmind.budgie.model.mapper.CurrencyMapper;
import io.geekmind.budgie.model.mapper.ProjectAccountMappingSettings;
import io.geekmind.budgie.model.mapper.StandardAccountMappingSettings;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class ProjectAccountToExistingProjectAccountMappingTest {

    private ProjectAccount sourceProjectAccount;
    private AccountCurrency sourceAccountCurrency;
    private ExistingProjectAccount resultExistingProjectAccount;

    @Before
    public void setUp() {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        CurrencyMapper mockedCurrencyMapper = mock(CurrencyMapper.class);
        new StandardAccountMappingSettings(mockedCurrencyMapper).configure(mapperFactory);
        new ProjectAccountMappingSettings(mockedCurrencyMapper).configure(mapperFactory);

        this.sourceProjectAccount = ProjectAccountFixture.travelToBahamas();
        this.sourceAccountCurrency = AccountCurrencyFixture.euro();
        doReturn(this.sourceAccountCurrency, this.sourceAccountCurrency)
            .when(mockedCurrencyMapper).mapFrom(any());
        this.resultExistingProjectAccount = mapperFactory.getMapperFacade().map(this.sourceProjectAccount, ExistingProjectAccount.class);
    }

    @Test
    public void idAttribute_isMapped() {
        assertThat(this.resultExistingProjectAccount)
            .hasFieldOrPropertyWithValue("id", this.sourceProjectAccount.getId());
    }

    @Test
    public void nameAttribute_isMapped() {
        assertThat(this.resultExistingProjectAccount)
            .hasFieldOrPropertyWithValue("name", this.sourceProjectAccount.getName());
    }

    @Test
    public void descriptionAttribute_isMapped() {
        assertThat(this.resultExistingProjectAccount)
            .hasFieldOrPropertyWithValue("description", this.sourceProjectAccount.getDescription());
    }

    @Test
    public void monthStartingAtAttribute_isMapped() {
        assertThat(this.resultExistingProjectAccount)
            .hasFieldOrPropertyWithValue("monthStartingAt", this.sourceProjectAccount.getMonthStartingAt());
    }

    @Test
    public void monthBillingDayAtAttribute_isMapped() {
        assertThat(this.resultExistingProjectAccount)
            .hasFieldOrPropertyWithValue("monthBillingDayAt", this.sourceProjectAccount.getMonthBillingDayAt());
    }

    @Test
    public void currencyAttribute_isMapped() {
        assertThat(this.resultExistingProjectAccount)
            .hasFieldOrPropertyWithValue("currency", this.sourceAccountCurrency);
    }

    @Test
    public void parentAttribute_isMapped() {
        assertThat(this.resultExistingProjectAccount)
            .hasFieldOrPropertyWithValue("parent.id", this.sourceProjectAccount.getParent().getId());
    }

}
