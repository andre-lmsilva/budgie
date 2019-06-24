package io.geekmind.budgie.model.mapper;

import io.geekmind.budgie.fixture.AccountFixture;
import io.geekmind.budgie.model.dto.ExistingAccount;
import io.geekmind.budgie.model.entity.Account;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class AccountToExistingAccountMapperTest {

    private Account sourceAccount;
    private ExistingAccount resultingExistingAccount;

    @Before
    public void setUp() {
        this.sourceAccount = AccountFixture.getMainAccount();
        this.resultingExistingAccount = new AccountToExistingAccountMapper().mapTo(this.sourceAccount);
    }

    @Test
    public void idAttributeIsMappedToExistingAccountIdAttribute() {
        assertThat(resultingExistingAccount)
            .hasFieldOrPropertyWithValue("id", this.sourceAccount.getId());
    }

    @Test
    public void nameAttributeIsMappedToExistingAccountNameAttribute() {
        assertThat(resultingExistingAccount)
            .hasFieldOrPropertyWithValue("name", this.sourceAccount.getName());
    }

    @Test
    public void descriptionAttributeIsMappedToExistingAccountDescriptionAttribute() {
        assertThat(resultingExistingAccount)
            .hasFieldOrPropertyWithValue("description", this.sourceAccount.getDescription());
    }

    @Test
    public void mainAccountAttributeIsMappedToExistingAccountMainAccountAttribute() {
        assertThat(resultingExistingAccount)
            .hasFieldOrPropertyWithValue("mainAccount", this.sourceAccount.getMainAccount());
    }

    @Test
    public void monthStartingAtAttributeIsMappedToExistingAccountMonthStartingAtAttribute() {
        assertThat(resultingExistingAccount)
            .hasFieldOrPropertyWithValue("monthStartingAt", this.sourceAccount.getMonthStartingAt());
    }

    @Test
    public void monthBillingDayAtIsMappedToExistingAccountMonthBillingDayAtAttribute() {
        assertThat(resultingExistingAccount)
            .hasFieldOrPropertyWithValue("monthBillingDayAt", this.sourceAccount.getMonthBillingDayAt());
    }

    @Test
    public void showBalanceOnMainAccountIsMappedToExistingAccountShowBalanceOnMainAccountAttribute() {
        assertThat(resultingExistingAccount)
            .hasFieldOrPropertyWithValue("showBalanceOnMainAccount", this.sourceAccount.getShowBalanceOnMainAccount());
    }

}