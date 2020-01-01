package io.geekmind.budgie.balance;

import io.geekmind.budgie.model.dto.ExistingRecord;
import io.geekmind.budgie.model.dto.balance.BalanceCalculationRequest;
import io.geekmind.budgie.repository.RecordService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * Retrieves all records within the calculated period, sort them by date in an ascending order and sets the list on
 * the balance being calculated. Because it depends on the balance dates already calculated, it mus be chained after
 * the {@link CalculateRegularBalanceDatesStep}.
 *
 * @author Andre Silva
 */
@Component("loadPeriodRecordsStep")
public class LoadPeriodRecordsStep extends BaseBalanceCalculationStep {

    private final RecordService recordService;

    public LoadPeriodRecordsStep(@Qualifier("generateDependantAccountRecordsStep") BaseBalanceCalculationStep nextChainedStep,
                                 RecordService recordService) {
        super(nextChainedStep);
        this.recordService = recordService;
    }

    @Override
    public void calculate(BalanceCalculationRequest balanceCalculationRequest) {
        balanceCalculationRequest.getBalance().setRecords(
            this.recordService.loadAll(
                balanceCalculationRequest.getBalance().getAccount().getId(),
                balanceCalculationRequest.getBalance().getBalanceDates().getPeriodStartDate(),
                balanceCalculationRequest.getBalance().getBalanceDates().getPeriodEndDate()
            )
        );

        balanceCalculationRequest.getBalance().setGroupedRecords(
            balanceCalculationRequest.getBalance().getRecords()
                .stream()
                .sorted(Comparator.comparing(ExistingRecord::getRecordDate))
                .collect(
                    Collectors.groupingBy(record -> record.getRecordDate())
                )
        );
    }

    @Override
    public Boolean shouldExecute(BalanceCalculationRequest balanceCalculationRequest) {
        return  balanceCalculationRequest != null &&
                balanceCalculationRequest.getBalance() != null &&
                balanceCalculationRequest.getBalance().getBalanceDates() != null &&
                balanceCalculationRequest.getBalance().getBalanceDates().getPeriodStartDate() != null &&
                balanceCalculationRequest.getBalance().getBalanceDates().getPeriodEndDate() != null;
    }
}
