package io.geekmind.budgie.fixture;

import io.geekmind.budgie.model.entity.Account;
import io.geekmind.budgie.model.entity.AccountParameter;
import io.geekmind.budgie.model.entity.AccountParameterKey;

import java.time.LocalDateTime;

public class AccountParameterFixture {

    public static AccountParameter currentLoginTimestamp() {
        AccountParameter accountParameter = new AccountParameter();
        accountParameter.setId(-1);
        accountParameter.setAccount(StandardAccountFixture.getMainAccount());
        accountParameter.setKey(AccountParameterKey.CURRENT_LOGIN_TIMESTAMP.name());
        accountParameter.setValue(LocalDateTime.now().toString());
        return accountParameter;
    }

}
