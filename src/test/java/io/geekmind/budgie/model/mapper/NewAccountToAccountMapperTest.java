package io.geekmind.budgie.model.mapper;

import io.geekmind.budgie.fixture.NewAccountFixture;
import io.geekmind.budgie.model.dto.NewAccount;
import io.geekmind.budgie.model.entity.Account;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class NewAccountToAccountMapperTest {

    private NewAccount newAccountSource;
    private Account resultingAccount;

    @Before
    public void setUp() {
        this.newAccountSource = NewAccountFixture.getMainAccount();
        this.resultingAccount = new NewAccountToAccountMapper().mapTo(this.newAccountSource);
    }

    @Test
    public void idIsNull() {
        assertThat(resultingAccount.getId()).isNull();
    }

    @Test
    public void nameIsMappedToAccountNameAttribute() {
        assertThat(resultingAccount)
            .hasFieldOrPropertyWithValue("name", this.newAccountSource.getName());
    }

    @Test
    public void descriptionIsMappedToAccountDescriptionAttribute() {
        assertThat(resultingAccount)
            .hasFieldOrPropertyWithValue("description", this.newAccountSource.getDescription());
    }

    @Test
    public void monthStartingAtIsMappedToAccountMonthStartingAtAttribute() {
        assertThat(resultingAccount)
            .hasFieldOrPropertyWithValue("monthStartingAt", this.newAccountSource.getMonthStartingAt());
    }

    @Test
    public void monthBillingDayAtIsMappedToAccountMonthBillingDayAtAttribute() {
        assertThat(resultingAccount)
            .hasFieldOrPropertyWithValue("monthBillingDayAt", this.newAccountSource.getMonthBillingDayAt());
    }

    @Test
    public void showBalanceOnMainAccountIsMappedToAccountShowBalanceOnMainAccountAttribute() {
        assertThat(resultingAccount)
            .hasFieldOrPropertyWithValue("showBalanceOnMainAccount", this.newAccountSource.getShowBalanceOnMainAccount());
    }

    @Test
    public void mainAccountAttributeIsAlwaysFalse() {
        assertThat(resultingAccount)
            .hasFieldOrPropertyWithValue("mainAccount", Boolean.FALSE);
    }

}