package io.geekmind.budgie.fixture;


import io.geekmind.budgie.model.dto.standard_account.ExistingStandardAccount;

import java.util.Collections;

public class ExistingAccountFixture {

    public static ExistingStandardAccount getMainAccount() {
        ExistingStandardAccount account = new ExistingStandardAccount();
        account.setId(-1);
        account.setName("Fake Existing Account");
        account.setDescription("Used only for test purposes.");
        account.setMonthStartingAt(1);
        account.setMonthBillingDayAt(5);
        account.setMainAccount(Boolean.TRUE);
        account.setShowBalanceOnParentAccount(Boolean.FALSE);
        account.setCurrency(AccountCurrencyFixture.euro());
        account.setDependants(Collections.singletonList(getSavingsAccount()));
        return account;
    }

    public static ExistingStandardAccount getSavingsAccount() {
        ExistingStandardAccount account = new ExistingStandardAccount();
        account.setId(-9);
        account.setName("Fake Savings Account");
        account.setDescription("Used only for test purposes.");
        account.setMonthStartingAt(1);
        account.setMonthBillingDayAt(5);
        account.setMainAccount(Boolean.FALSE);
        account.setShowBalanceOnParentAccount(Boolean.TRUE);
        account.setCurrency(AccountCurrencyFixture.euro());
        return account;
    }
}
