package io.geekmind.budgie.fixture;

import io.geekmind.budgie.model.dto.NewAccount;

public class NewAccountFixture {

    public static NewAccount getMainAccount() {
        return new NewAccount(
            "Fake Account",
            "To be used exclusively for test purpose.",
            1,
            5,
            Boolean.FALSE
        );
    }

}
