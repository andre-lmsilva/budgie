package io.geekmind.budgie.fixture;

import io.geekmind.budgie.model.dto.AccountCurrency;

public class AccountCurrencyFixture {

    public static AccountCurrency euro() {
        return new AccountCurrency("EUR", "Euro", "€");
    }

}
