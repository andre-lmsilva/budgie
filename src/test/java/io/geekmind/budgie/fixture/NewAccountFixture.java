package io.geekmind.budgie.fixture;


import io.geekmind.budgie.model.dto.account.NewAccount;

public class NewAccountFixture {

    public static NewAccount getMainAccount() {
        NewAccount account = new NewAccount();
        account.setId(-999);
        account.setName("Updated Fake Account");
        account.setDescription("To be used exclusively for test update functionality purposes.");
        account.setMonthStartingAt(28);
        account.setMonthBillingDayAt(28);
        account.setShowBalanceOnMainAccount(true);
        account.setCurrencyCode("BRL");
        return account;
    }

}
