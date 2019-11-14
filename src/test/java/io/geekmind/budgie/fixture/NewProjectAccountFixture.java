package io.geekmind.budgie.fixture;

import io.geekmind.budgie.model.dto.project_account.NewProjectAccount;

import java.math.BigDecimal;

public class NewProjectAccountFixture {

    public static NewProjectAccount travelToBahamas() {
        NewProjectAccount account = new NewProjectAccount();
        account.setName("Travel to Bahamas");
        account.setDescription("This is the project to save money to travel to bahamas.");
        account.setParentId(-1);
        account.setTargetValue(BigDecimal.TEN);
        return account;
    }

}
