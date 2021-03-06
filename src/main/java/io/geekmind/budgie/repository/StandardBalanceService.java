package io.geekmind.budgie.repository;

import io.geekmind.budgie.balance.BaseBalanceCalculationStep;
import io.geekmind.budgie.balance.commons.BalanceDatesCalculator;
import io.geekmind.budgie.model.dto.balance.Balance;
import io.geekmind.budgie.model.dto.balance.BalanceCalculationRequest;
import io.geekmind.budgie.model.dto.balance.BalanceType;
import io.geekmind.budgie.model.dto.standard_account.ExistingStandardAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Provides the capability to calculate the balance of an account for a certain period of the time. This class adopts
 * the facade pattern as design to address all the requirements related with the task to calculate the account balance.
 *
 * @author Andre Silva
 */
@Service
public class StandardBalanceService {

    private final StandardAccountService standardAccountService;
    private final BaseBalanceCalculationStep calculationChainEntryPoint;
    private final BalanceDatesCalculator balanceDatesCalculator;

    @Autowired
    public StandardBalanceService(StandardAccountService standardAccountService,
                                  @Qualifier("balanceCalculationChainEntryPoint") BaseBalanceCalculationStep calculationChainEntryPoint,
                                  BalanceDatesCalculator balanceDatesCalculator) {
        this.standardAccountService = standardAccountService;
        this.calculationChainEntryPoint = calculationChainEntryPoint;
        this.balanceDatesCalculator = balanceDatesCalculator;
    }

    /**
     * Calculates the balance of an account based on a date of reference. This method acts as a facade, delegating to
     * the other class methods the real task to calculate the values.
     *
     * @param accountId         Id of the account which balance is being calculated.
     * @param referenceDate     Period reference date. It is optional. The current date will be assume when null.
     * @return
     */
    public Balance generateBalance(Integer accountId, LocalDate referenceDate, BalanceType balanceType) {
        return this.retrieveAccount(accountId)
            .map((ExistingStandardAccount account) -> {
                LocalDate refDate = Optional.ofNullable(referenceDate).orElse(LocalDate.now());
                BalanceCalculationRequest.BalanceCalculationRequestBuilder requestBuilder =
                        new BalanceCalculationRequest.BalanceCalculationRequestBuilder();

                requestBuilder.withBalanceType(balanceType)
                    .withAccount(account)
                    .withReferenceDate(refDate);

                if (account.getDependants() != null && !account.getDependants().isEmpty() && !balanceType.equals(BalanceType.BUDGET_TEMPLATE_BALANCE)) {
                    this.calculateDependantAccountsBalancesFor(account, refDate)
                        .forEach(requestBuilder::addDependantAccountBalance);
                }

                BalanceCalculationRequest request = requestBuilder.build();
                this.calculationChainEntryPoint.proceed(request);
                return request.getBalance();
            }).orElse(new Balance());
    }

    /**
     * Retrieves all accounts dependant from the received account, calculates its balance for the same period and returns
     * all of them as a list.
     *
     * @param account           Main account.
     * @param referenceDate     Reference date of the period being calculated.
     * @return List containing the balances of all the account dependant accounts for the same period.
     */
    protected List<Balance> calculateDependantAccountsBalancesFor(ExistingStandardAccount account, LocalDate referenceDate) {
        LocalDate mainAccountPeriodEndDate = this.balanceDatesCalculator.calculatePeriodEndDate(referenceDate, account);
        return account.getActiveDependantAccounts()
            .stream()
            .map(dependantAccount -> {
                LocalDate periodBillingDate = mainAccountPeriodEndDate.withDayOfMonth(dependantAccount.getMonthBillingDayAt());
                LocalDate accountPeriodEndDate = this.balanceDatesCalculator.calculatePeriodEndDateBasedOnBillingDate(periodBillingDate, dependantAccount);
                return this.generateBalance(dependantAccount.getId(), accountPeriodEndDate, BalanceType.REGULAR_PERIOD_BALANCE);
            })
            .collect(Collectors.toList());
    }

    /**
     * Retrieves the account which the balance is being calculated. It retrieves the main account by default, which is
     * triggered when the received account id is null.
     * @param accountId Id of the account which balance is being calculated.
     * @return The retrieved account or null when the received id does not exists.
     */
    protected Optional<ExistingStandardAccount> retrieveAccount(Integer accountId) {
        if (null == accountId) {
            return this.standardAccountService.getMainAccount();
        } else {
            return this.standardAccountService.loadById(accountId);
        }
    }
}
