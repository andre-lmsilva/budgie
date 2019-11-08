package io.geekmind.budgie.fixture;

import io.geekmind.budgie.model.dto.standard_account.EditStandardAccount;

public class EditAccountFixture {

    public static EditStandardAccount savingsAccount() {
        EditStandardAccount editAccount = new EditStandardAccount();
        editAccount.setId(-321);
        editAccount.setParentId(-987);
        editAccount.setName("Fake Savings Account");
        editAccount.setDescription("Fake Savings Account Description.");
        editAccount.setMonthStartingAt(1);
        editAccount.setMonthBillingDayAt(1);
        editAccount.setShowBalanceOnParentAccount(true);
        return editAccount;
    }

}
