package io.geekmind.budgie.fixture;

import io.geekmind.budgie.model.dto.balance.Balance;
import io.geekmind.budgie.model.dto.balance.BalanceType;

import java.util.ArrayList;

public class BalanceFixture {

    public static Balance getCurrentPeriodBalance() {
        Balance balance = new Balance();
        balance.setBalanceDates(BalanceDatesFixture.getBalanceDatesForCurrentPeriod());
        balance.setSummary(BalanceSummaryFixture.get());
        balance.setAccount(ExistingStandardAccountFixture.getMainAccount());
        balance.setRecords(new ArrayList<>());
        balance.setApplicableBudgetTemplateRecords(new ArrayList<>());
        balance.setBalanceType(BalanceType.REGULAR_PERIOD_BALANCE);
        return balance;
    }
}
