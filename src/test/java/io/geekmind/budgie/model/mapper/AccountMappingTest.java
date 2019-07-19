package io.geekmind.budgie.model.mapper;

import io.geekmind.budgie.fixture.AccountFixture;
import io.geekmind.budgie.fixture.ExistingAccountFixture;
import io.geekmind.budgie.fixture.NewAccountFixture;
import io.geekmind.budgie.model.dto.ExistingAccount;
import io.geekmind.budgie.model.dto.NewAccount;
import io.geekmind.budgie.model.entity.Account;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountMappingTest {

    private MapperFacade mapper;

    @Before
    public void setUp() {
        MapperFactory factory = new DefaultMapperFactory.Builder().build();
        new AccountMappingSettings().configure(factory);
        this.mapper = factory.getMapperFacade();
    }

    @Test
    public void mapping_FromAnAccountToNewExistingAccountInstance_MapsAllAttributes() {
        Account fakeAccount = AccountFixture.getMainAccount();
        ExistingAccount result = this.mapper.map(fakeAccount, ExistingAccount.class);
        assertThat(result).isEqualToComparingFieldByField(fakeAccount);
    }

    @Test
    public void mapping_FromNewAccountInstanceToAccountInstance_MapsAllAttributesExceptIdAndMainAccountAttributes() {
        NewAccount fakeNewAccount = NewAccountFixture.getMainAccount();
        Account result = this.mapper.map(fakeNewAccount, Account.class);
        assertThat(result)
            .hasFieldOrPropertyWithValue("id", null)
            .hasFieldOrPropertyWithValue("name", fakeNewAccount.getName())
            .hasFieldOrPropertyWithValue("description", fakeNewAccount.getDescription())
            .hasFieldOrPropertyWithValue("monthStartingAt", fakeNewAccount.getMonthStartingAt())
            .hasFieldOrPropertyWithValue("monthBillingDayAt", fakeNewAccount.getMonthBillingDayAt())
            .hasFieldOrPropertyWithValue("showBalanceOnMainAccount", fakeNewAccount.getShowBalanceOnMainAccount())
            .hasFieldOrPropertyWithValue("mainAccount", false);
    }

    @Test
    public void merging_FromExistingAccountInstanceToAlreadyFilledAccountInstance_DoesNotMapsNullValues() {
        ExistingAccount fakeExistingAccount = ExistingAccountFixture.getSavingsAccount();
        fakeExistingAccount.setDescription(null);
        Account fakeAccount = AccountFixture.getMainAccount();

        this.mapper.map(fakeExistingAccount, fakeAccount);
        assertThat(fakeAccount)
            .hasFieldOrPropertyWithValue("id", fakeExistingAccount.getId())
            .hasFieldOrPropertyWithValue("name", fakeExistingAccount.getName())
            .hasFieldOrPropertyWithValue("monthStartingAt", fakeExistingAccount.getMonthStartingAt())
            .hasFieldOrPropertyWithValue("monthBillingDayAt", fakeExistingAccount.getMonthBillingDayAt())
            .hasFieldOrPropertyWithValue("showBalanceOnMainAccount", fakeExistingAccount.getShowBalanceOnMainAccount())
            .hasFieldOrProperty("description");
        assertThat(fakeAccount.getMainAccount()).isNotEqualTo(fakeExistingAccount.getMainAccount());
    }

}
