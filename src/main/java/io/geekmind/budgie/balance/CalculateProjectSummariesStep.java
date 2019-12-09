package io.geekmind.budgie.balance;

import io.geekmind.budgie.model.dto.ExistingRecord;
import io.geekmind.budgie.model.dto.balance.BalanceCalculationRequest;
import io.geekmind.budgie.model.dto.balance.ProjectBalanceSummary;
import io.geekmind.budgie.model.dto.project_account.ExistingProjectAccount;
import io.geekmind.budgie.repository.RecordService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * This step summarizes the balance of the projects attached to the account and add to the balance being calculated.
 *
 * @author Andre Silva
 */
@Component("calculateProjectSummariesStep")
public class CalculateProjectSummariesStep extends BaseBalanceCalculationStep {

    public static final LocalDate START_DATE = LocalDate.of(1970, 1, 1);

    private final RecordService recordService;

    public CalculateProjectSummariesStep(RecordService recordService) {
        super(null);
        this.recordService = recordService;
    }

    /*
     * For each project account attached to the account which balance is being calculated, calculates is summary for the
     * balance period. The start date is always {@link #START_DATE}. The end date can be (1) the current date when the
     * period end date is in the future, or (2) the period end date when it is in the past.
     *
     * @param balanceCalculationRequest Instance of the balance calculation request being processed.
     */
    @Override
    public void calculate(BalanceCalculationRequest balanceCalculationRequest) {
        final LocalDate endDate = this.calculateEndDate(balanceCalculationRequest);

        balanceCalculationRequest.getBalance().setProjectBalanceSummaries(new ArrayList<>());
        balanceCalculationRequest.getBalance().getAccount().getActiveProjectAccounts()
            .forEach(projectAccount -> {
                BigDecimal balance = this.calculateBalanceFor(projectAccount, START_DATE, endDate);
                BigDecimal progress = balance
                    .divide(projectAccount.getTargetValue(), RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100D));

                ProjectBalanceSummary summary = new ProjectBalanceSummary(
                    projectAccount,
                    balance,
                    progress
                );

                balanceCalculationRequest.getBalance().getProjectBalanceSummaries().add(summary);
            });
    }

    /**
     * Calculates the end date to summarize the records for the balance being calculated. The current date is
     * taken as base and, if the balance period end date occurs before the current date, it takes the balance
     * period end date in consideration instead.
     *
     * @param balanceCalculationRequest Details about the balance being calculated.
     * @return A date calculated as aforementioned.
     */
    protected LocalDate calculateEndDate(BalanceCalculationRequest balanceCalculationRequest) {
        LocalDate endDate = LocalDate.now();
        if (balanceCalculationRequest.getBalance().getBalanceDates().getPeriodEndDate().isBefore(endDate)) {
            endDate = balanceCalculationRequest.getBalance().getBalanceDates().getPeriodEndDate();
        }
        return endDate;
    }

    /**
     * Calculates the project balance for the received period.
     *
     * @param account       Project account to have its balance calculated.
     * @param startDate     Period start date.
     * @param endDate       Period end date.
     * @return Summarized project account balance for the specific period/
     */
    protected BigDecimal calculateBalanceFor(ExistingProjectAccount account, final LocalDate startDate, final LocalDate endDate) {
        return this.recordService
            .loadAll(account.getId(), startDate, endDate)
            .stream()
            .map(ExistingRecord::getRecordValue)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Checks if this step should be executed. If the account being processed has associated active project accounts, it
     * will return true.
     *
     * @param balanceCalculationRequest Balance calculation request being processed.
     * @return  <i>true</i> when the account being processed has active project accounts associated. Otherwise, <i>false</i>.
     */
    @Override
    public Boolean shouldExecute(BalanceCalculationRequest balanceCalculationRequest) {
        return !balanceCalculationRequest.getBalance().getAccount().getActiveProjectAccounts().isEmpty();
    }
}
