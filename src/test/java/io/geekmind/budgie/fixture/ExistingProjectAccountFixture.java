package io.geekmind.budgie.fixture;

import io.geekmind.budgie.model.dto.project_account.ExistingProjectAccount;

import java.math.BigDecimal;

public class ExistingProjectAccountFixture {

    public static ExistingProjectAccount travelToBahamas() {
        ExistingProjectAccount projectAccount = new ExistingProjectAccount();
        projectAccount.setId(-987);
        projectAccount.setName("Travel to Bahamas");
        projectAccount.setDescription("Save money to travel to Bahamas next year.");
        projectAccount.setMonthStartingAt(1);
        projectAccount.setMonthBillingDayAt(1);
        projectAccount.setParent(ExistingStandardAccountFixture.getSavingsAccount());
        projectAccount.setCurrency(AccountCurrencyFixture.euro());
        projectAccount.setTargetValue(BigDecimal.TEN);
        projectAccount.setArchived(Boolean.FALSE);
        return projectAccount;
    }

}
