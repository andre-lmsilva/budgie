package io.geekmind.budgie.fixture;

import io.geekmind.budgie.model.dto.account.EditAccount;

public class EditAccountFixture {

    public static EditAccount savingsAccount() {
        EditAccount editAccount = new EditAccount();
        editAccount.setId(-321);
        editAccount.setParentId(-987);
        editAccount.setName("Fake Savings Account");
        editAccount.setDescription("Fake Savings Account Description.");
        editAccount.setMonthStartingAt(1);
        editAccount.setMonthBillingDayAt(1);
        editAccount.setShowBalanceOnMainAccount(true);
        return editAccount;
    }

}
