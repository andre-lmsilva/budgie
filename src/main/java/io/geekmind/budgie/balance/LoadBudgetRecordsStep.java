package io.geekmind.budgie.balance;

import io.geekmind.budgie.model.dto.balance.BalanceCalculationRequest;
import io.geekmind.budgie.model.dto.balance.BalanceType;
import io.geekmind.budgie.model.dto.ExistingRecord;
import io.geekmind.budgie.repository.BudgetTemplateRecordService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Loads all the applicable {@link io.geekmind.budgie.model.entity.BudgetTemplateRecord} for the account in the period.
 *
 * @author Andre Silva
 */
@Component("loadBudgetRecordsStep")
public class LoadBudgetRecordsStep extends BaseBalanceCalculationStep {

    private final BudgetTemplateRecordService budgetTemplateRecordService;

    public LoadBudgetRecordsStep(BudgetTemplateRecordService budgetTemplateRecordService) {
        super(null);
        this.budgetTemplateRecordService = budgetTemplateRecordService;
    }

    /**
     * Based on the container id of the records within the period, filters all the {@link io.geekmind.budgie.model.entity.BudgetTemplateRecord}
     * that doesn't have its container id present in the period yet.
     *
     * @param balanceCalculationRequest Instance of the balance calculation request being processed.
     */
    @Override
    public void calculate(BalanceCalculationRequest balanceCalculationRequest) {
        List<Integer> alreadyAppliedBudgetTemplateRecords = balanceCalculationRequest
            .getBalance()
            .getRecords()
            .stream()
            .filter(record -> record.getContainerId() != null)
            .map(ExistingRecord::getContainerId)
            .collect(Collectors.toList());

        List<ExistingRecord> applicableBudgetTemplateRecords = this.budgetTemplateRecordService
            .loadAllFromAccount(balanceCalculationRequest.getBalance().getAccount().getId())
            .stream()
            .filter(record -> !alreadyAppliedBudgetTemplateRecords.contains(record.getContainerId()))
            .collect(Collectors.toList());

        balanceCalculationRequest.getBalance().setApplicableBudgetTemplateRecords(
            applicableBudgetTemplateRecords
        );
    }

    @Override
    public Boolean shouldExecute(BalanceCalculationRequest balanceCalculationRequest) {
        return null != balanceCalculationRequest &&
                null != balanceCalculationRequest.getBalance() &&
                null != balanceCalculationRequest.getBalance().getBalanceType() &&
                !balanceCalculationRequest.getBalance().getBalanceType().equals(BalanceType.BUDGET_TEMPLATE_BALANCE) &&
                null != balanceCalculationRequest.getBalance().getAccount() &&
                null != balanceCalculationRequest.getBalance().getRecords() &&
                null != balanceCalculationRequest.getBalance().getBalanceDates() &&
                null != balanceCalculationRequest.getBalance().getBalanceDates().getPeriodEndDate() &&
                balanceCalculationRequest.getBalance().getBalanceDates().getPeriodEndDate().compareTo(LocalDate.now()) >= 0;
    }
}
