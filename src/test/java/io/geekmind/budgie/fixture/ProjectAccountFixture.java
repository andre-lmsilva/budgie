package io.geekmind.budgie.fixture;

import io.geekmind.budgie.model.entity.ProjectAccount;
import io.geekmind.budgie.model.entity.StandardAccount;

import java.util.Collections;

public class ProjectAccountFixture {

    public static ProjectAccount travelToBahamas() {
        StandardAccount parentAccount = StandardAccountFixture.getMainAccount();

        ProjectAccount projectAccount = new ProjectAccount();
        projectAccount.setId(-999);
        projectAccount.setName("Travel to Bahamas");
        projectAccount.setDescription("Saving for travel to Bahamas.");
        projectAccount.setCurrencyCode(parentAccount.getCurrencyCode());
        projectAccount.setMonthStartingAt(parentAccount.getMonthStartingAt());
        projectAccount.setMonthBillingDayAt(parentAccount.getMonthBillingDayAt());
        projectAccount.setParent(parentAccount);
        projectAccount.setShowBalanceOnParentAccount(Boolean.TRUE);
        projectAccount.setParameters(
            Collections.singletonList(
                AccountParameterFixture.currentLoginTimestamp()
            )
        );
        return projectAccount;
    }

}
