package io.geekmind.budgie.fixture;

import io.geekmind.budgie.model.dto.ExistingAccount;

public class ExistingAccountFixture {

    public static ExistingAccount getMainAccount() {
        return new ExistingAccount(
            -1,
            "Fake Existing Account",
            "Used only for test purposes.",
            1,
            5,
           Boolean.TRUE,
           Boolean.FALSE
        );
    }

    public static ExistingAccount getSavingsAccount() {
        return new ExistingAccount(
            -9,
            "Fake Savings Account",
            "Used only for test purposes.",
            1,
            5,
            Boolean.FALSE,
            Boolean.TRUE
        );
    }
}
