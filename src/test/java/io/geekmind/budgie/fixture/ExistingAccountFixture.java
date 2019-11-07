package io.geekmind.budgie.fixture;

import io.geekmind.budgie.model.dto.account.ExistingAccount;

public class ExistingAccountFixture {

    public static ExistingAccount getMainAccount() {
        ExistingAccount account = new ExistingAccount();
        account.setId(-1);
        account.setName("Fake Existing Account");
        account.setDescription("Used only for test purposes.");
        account.setMonthStartingAt(1);
        account.setMonthBillingDayAt(5);
        account.setMainAccount(Boolean.TRUE);
        account.setShowBalanceOnParentAccount(Boolean.FALSE);
        account.setCurrency(AccountCurrencyFixture.euro());
        return account;
    }

    public static ExistingAccount getSavingsAccount() {
        ExistingAccount account = new ExistingAccount();
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
