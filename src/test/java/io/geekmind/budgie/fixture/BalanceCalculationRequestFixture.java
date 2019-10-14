package io.geekmind.budgie.fixture;

import io.geekmind.budgie.model.dto.BalanceCalculationRequest;

import java.util.HashMap;

public class BalanceCalculationRequestFixture {

    public static BalanceCalculationRequest get() {
        BalanceCalculationRequest request = new BalanceCalculationRequest();
        request.setBalance(BalanceFixture.getCurrentPeriodBalance());
        request.setDependantAccountBalances(new HashMap<>());
        return request;
    }

}
