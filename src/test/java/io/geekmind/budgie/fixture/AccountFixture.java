package io.geekmind.budgie.fixture;

import io.geekmind.budgie.model.entity.Account;

import java.util.ArrayList;

public class AccountFixture {

    public static Account getMainAccount() {
        Account account = new Account();
        account.setId(-1);
        account.setName("Fake Main Account");
        account.setDescription("Main account used exclusively for test purposes.");
        account.setMainAccount(Boolean.TRUE);
        account.setShowBalanceOnParentAccount(Boolean.FALSE);
        account.setMonthStartingAt(1);
        account.setMonthBillingDayAt(31);
        account.setCurrencyCode("EUR");
        return account;
    }

    public static Account creditCard() {
        Account account = new Account();
        account.setId(-2);
        account.setName("Fake Credit Card");
        account.setDescription("Fake credit card account used for test purposes.");
        account.setShowBalanceOnParentAccount(Boolean.TRUE);
        account.setMonthStartingAt(1);
        account.setMonthBillingDayAt(1);
        account.setCurrencyCode("EUR");
        account.setParent(getMainAccount());
        account.setDependants(new ArrayList<>());
        account.getDependants().add(
            internationalCreditCardExpenses(account)
        );
        return account;
    }

    public static Account internationalCreditCardExpenses(Account parent) {
        Account account = new Account();
        account.setId(-3);
        account.setParent(parent);
        account.setName("Credit Card International Expenses");
        account.setDescription("Fake credit card international expenses account.");
        account.setShowBalanceOnParentAccount(Boolean.TRUE);
        account.setMonthStartingAt(1);
        account.setMonthBillingDayAt(1);
        account.setCurrencyCode("EUR");
        return account;
    }

}
