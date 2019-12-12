package io.geekmind.budgie.fixture;

import io.geekmind.budgie.model.dto.account_parameter.ExistingAccountParameter;
import io.geekmind.budgie.model.entity.AccountParameterKey;

import java.time.LocalDateTime;

public class ExistingAccountParameterFixture {

    public static ExistingAccountParameter lastLoginTimestamp() {
        ExistingAccountParameter existingAccountParameter = new ExistingAccountParameter();
        existingAccountParameter.setId(-1);
        existingAccountParameter.setAccount(ExistingStandardAccountFixture.getSavingsAccount());
        existingAccountParameter.setKey(AccountParameterKey.LAST_LOGIN_TIMESTAMP);
        existingAccountParameter.setValue(LocalDateTime.now().toString());
        return existingAccountParameter;
    }

}
