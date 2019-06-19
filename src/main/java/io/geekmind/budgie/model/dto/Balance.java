package io.geekmind.budgie.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class Balance {

    private ExistingAccount account;

    private BalanceDates balanceDates;

    private List<ExistingRecord> records;

    private BalanceSummary summary;

}
