package io.geekmind.budgie.model.dto.balance;

import io.geekmind.budgie.model.dto.ExistingRecord;
import io.geekmind.budgie.model.dto.standard_account.ExistingStandardAccount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Carry all the details required to present the balance of an account during a certain period of time.
 *
 * @author Andre Silva
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Balance {

    private ExistingStandardAccount account;

    private BalanceDates balanceDates;

    private List<ExistingRecord> records;

    private BalanceSummary summary = new BalanceSummary();

    private List<CategoryBalanceSummary> categoryBalanceSummaries;

    private List<ExistingRecord> applicableBudgetTemplateRecords;

    private BalanceType balanceType = BalanceType.REGULAR_PERIOD_BALANCE;

    private List<ProjectBalanceSummary> projectBalanceSummaries;

    private Map<LocalDate, List<ExistingRecord>> groupedRecords;

}
