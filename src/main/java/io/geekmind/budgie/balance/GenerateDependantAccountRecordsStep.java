package io.geekmind.budgie.balance;

import io.geekmind.budgie.model.dto.balance.BalanceCalculationRequest;
import io.geekmind.budgie.model.dto.balance.BalanceType;
import io.geekmind.budgie.model.dto.DependantAccountRecord;
import io.geekmind.budgie.model.dto.ExistingRecord;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Calculates the balance of main account's dependant accounts and add it as a balance record.
 *
 * @author Andre Silva
 */
@Component("generateDependantAccountRecordsStep")
public class GenerateDependantAccountRecordsStep extends BaseBalanceCalculationStep {

    private final MapperFacade mapper;

    public GenerateDependantAccountRecordsStep(@Qualifier("calculateBalanceSummaryStep") BaseBalanceCalculationStep nextChainedStep,
                                               MapperFacade mapper) {
        super(nextChainedStep);
        this.mapper = mapper;
    }

    @Override
    public void calculate(BalanceCalculationRequest balanceCalculationRequest) {
        List<ExistingRecord> dependantAccountRecords = balanceCalculationRequest.getDependantAccountBalances()
            .values()
            .stream()
            .filter(balance -> balance.getSummary().getFinalBalance().compareTo(BigDecimal.ZERO) != 0)
            .map(balance -> this.mapper.map(balance, DependantAccountRecord.class))
            .collect(Collectors.toList());

        if (null == balanceCalculationRequest.getBalance().getRecords()) {
            balanceCalculationRequest.getBalance().setRecords(new ArrayList<>());
        }
        balanceCalculationRequest.getBalance().getRecords().addAll(dependantAccountRecords);
        balanceCalculationRequest.getBalance().getRecords().sort(Comparator.comparing(ExistingRecord::getRecordDate));
    }

    @Override
    public Boolean shouldExecute(BalanceCalculationRequest balanceCalculationRequest) {
        return null != balanceCalculationRequest &&
                null != balanceCalculationRequest.getBalance() &&
                null != balanceCalculationRequest.getBalance().getBalanceType() &&
                !balanceCalculationRequest.getBalance().getBalanceType().equals(BalanceType.BUDGET_TEMPLATE_BALANCE) &&
                null != balanceCalculationRequest.getBalance().getAccount() &&
                balanceCalculationRequest.getBalance().getAccount().getMainAccount() &&
                null != balanceCalculationRequest.getDependantAccountBalances() &&
                !balanceCalculationRequest.getDependantAccountBalances().values().isEmpty();
    }
}
