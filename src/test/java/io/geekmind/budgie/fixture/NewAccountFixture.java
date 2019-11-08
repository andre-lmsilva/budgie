package io.geekmind.budgie.fixture;


import io.geekmind.budgie.model.dto.standard_account.NewStandardAccount;

public class NewAccountFixture {

    public static NewStandardAccount getMainAccount() {
        NewStandardAccount account = new NewStandardAccount();
        account.setName("Updated Fake Account");
        account.setParentId(-999);
        account.setDescription("To be used exclusively for test update functionality purposes.");
        account.setMonthStartingAt(28);
        account.setMonthBillingDayAt(28);
        account.setShowBalanceOnParentAccount(true);
        account.setCurrencyCode("BRL");
        return account;
    }

}
