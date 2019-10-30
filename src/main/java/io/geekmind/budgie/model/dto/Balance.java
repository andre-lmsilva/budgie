package io.geekmind.budgie.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Carry all the details required to present the balance of an account during a certain period of time.
 *
 * @author Andre Silva
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Balance {

    private ExistingAccount account;

    private BalanceDates balanceDates;

    private List<ExistingRecord> records;

    private BalanceSummary summary = new BalanceSummary();

    private List<CategoryBalanceSummary> categoryBalanceSummaries;

    private List<ExistingRecord> applicableBudgetTemplateRecords;

    private BalanceType balanceType = BalanceType.REGULAR_PERIOD_BALANCE;

}
