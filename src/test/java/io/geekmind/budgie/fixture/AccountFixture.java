package io.geekmind.budgie.fixture;

import io.geekmind.budgie.model.entity.Account;

public class AccountFixture {

    public static Account getMainAccount() {
        Account account = new Account();
        account.setId(-1);
        account.setName("Fake Main Account");
        account.setDescription("Main account used exclusively for test purposes.");
        account.setMainAccount(Boolean.TRUE);
        account.setShowBalanceOnMainAccount(Boolean.FALSE);
        account.setMonthStartingAt(1);
        account.setMonthBillingDayAt(31);
        return account;
    }

}
