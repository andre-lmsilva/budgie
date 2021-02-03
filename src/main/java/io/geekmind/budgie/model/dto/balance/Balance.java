package io.geekmind.budgie.model.dto.balance;

import io.geekmind.budgie.model.dto.ExistingRecord;
import io.geekmind.budgie.model.dto.standard_account.ExistingStandardAccount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

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

    public Map<LocalDate, List<ExistingRecord>> getGroupedRecords() {
        return Optional.ofNullable(this.getRecords())
            .map(records ->
                new TreeMap(
                    records.stream().collect(Collectors.groupingBy(ExistingRecord::getRecordDate))
                )
            ).orElse(new TreeMap());
    }

}
