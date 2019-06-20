package io.geekmind.budgie.repository;

import io.geekmind.budgie.model.dto.*;
import io.geekmind.budgie.model.mapper.BalanceToDependantAccountRecordMapper;
import io.geekmind.budgie.model.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
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

    private final AccountService accountService;
    private final RecordService recordService;
    private final Mapper<Balance, DependantAccountRecord> dependantAccountRecordMapper;

    @Autowired
    public StandardBalanceService(AccountService accountService,
                                  RecordService recordService,
                                  @Qualifier(BalanceToDependantAccountRecordMapper.QUALIFIER)Mapper<Balance, DependantAccountRecord> dependantAccountRecordMapper) {
        this.accountService = accountService;
        this.recordService = recordService;
        this.dependantAccountRecordMapper = dependantAccountRecordMapper;
    }

    /**
     * Calculates the balance of an account based on a date of reference. This method acts as a facade, delegating to
     * the other class methods the real task to calculate the values.
     *
     * @param accountId         Id of the account which balance is being calculated.
     * @param referenceDate
     * @return
     */
    public Balance generateBalance(Integer accountId, LocalDate referenceDate) {
        Balance balance = new Balance();
        Optional<ExistingAccount> retrievedAccount = this.retrieveAccount(accountId);
        if (retrievedAccount.isPresent()) {
            ExistingAccount existingAccount = retrievedAccount.get();
            balance.setAccount(existingAccount);
            balance.setBalanceDates(
                this.calculateBalanceDates(existingAccount, referenceDate)
            );
            balance.setRecords(this.loadAccountRecords(existingAccount, balance.getBalanceDates()));
            balance.setSummary(
                this.calculateBalanceSummary(
                    balance.getRecords()
                )
            );
        }
        return balance;
    }

    /**
     * Retrieves the account which the balance is being calculated. It retrieves the main account by default, which is
     * triggered when the received account id is null.
     * @param accountId Id of the account which balance is being calculated.
     * @return The retrieved account or null when the received id does not exists.
     */
    protected Optional<ExistingAccount> retrieveAccount(Integer accountId) {
        if (null == accountId) {
            return this.accountService.getMainAccount();
        } else {
            return this.accountService.loadById(accountId);
        }
    }

    /**
     * Calculates the following dates based on the account details and on the received reference date, and returns a
     * new instance of {@link BalanceDates} containing the calculated dates.
     *
     * <ol>
     *     <li>Period start date.</li>
     *     <li>Period end date.</li>
     *     <li>Period billing date.</li>
     *     <li>Reference date for the previous period.</li>
     *     <li>Reference date for the next period.</li>
     * </ol>
     *
     * @param account           Account details used to calculate the aforementioned dates.
     * @param referenceDate     Reference date to calculate the aforementioned dates.
     * @return {@link BalanceDates} containing the aforementioned calculated dates.
     */
    protected BalanceDates calculateBalanceDates(final ExistingAccount account, LocalDate referenceDate) {
        LocalDate refDate = LocalDate.now();
        if (null != referenceDate) {
            refDate = referenceDate;
        }

        LocalDate periodEndDate = this.calculatePeriodEndDate(account, refDate);
        LocalDate periodStartDate = this.calculatePeriodStartDate(periodEndDate);
        LocalDate periodBillingDate = this.calculatePeriodBillingDate(account, periodEndDate);

        BalanceDates balanceDates = new BalanceDates();
        balanceDates.setPeriodStartDate(periodStartDate);
        balanceDates.setPeriodEndDate(periodEndDate);
        balanceDates.setPeriodBillingDate(periodBillingDate);
        balanceDates.setReferenceDate(refDate);
        balanceDates.setPreviousPeriodStartDate(refDate.minusMonths(1L));
        balanceDates.setNextPeriodStartDate(refDate.plusMonths(1L));
        return balanceDates;
    }

    /**
     * Calculates the date when the period ends based on the account details and on a reference date. It is always one
     * day before the start of the next period.
     * @param account       Account details.
     * @param referenceDate Date of reference to calculate the end of the period.
     * @return New date containing the end date of the period which the reference date is comprehended.
     */
    protected LocalDate calculatePeriodEndDate(ExistingAccount account, LocalDate referenceDate) {
        LocalDate periodEndDate = referenceDate;
        int dayOfMonth = 31;
        if (account.getMonthStartingAt() > 1) {
            dayOfMonth = account.getMonthStartingAt() - 1;
        }

        if (dayOfMonth < referenceDate.getDayOfMonth()) {
            periodEndDate = periodEndDate.plusMonths(1);
        }

        if (periodEndDate.lengthOfMonth() < dayOfMonth) {
            dayOfMonth = periodEndDate.lengthOfMonth();
        }

        return periodEndDate.withDayOfMonth(dayOfMonth);
    }

    /**
     * Calculates the start date of the period based on the account details and on the period date.
     * @param periodEndDate Period end date.
     * @return New date with the start date calculated to be one month before the period end date.
     */
    protected LocalDate calculatePeriodStartDate(LocalDate periodEndDate) {
        if (periodEndDate.getDayOfMonth() == periodEndDate.lengthOfMonth()) {
            return periodEndDate.withDayOfMonth(1);
        }
        return periodEndDate.minusMonths(1L).plusDays(1L);
    }

    /**
     * Calculates the billing day of a period based on the account details and the end of the period being calculated.
     * @param account       Account details.
     * @param periodEndDate End date of the period being calculated.
     * @return Billing date calculated based on the end date of the period being calculated.
     */
    protected LocalDate calculatePeriodBillingDate(ExistingAccount account, LocalDate periodEndDate) {
        LocalDate billingDate = periodEndDate;
        if (account.getMonthBillingDayAt() < billingDate.getDayOfMonth()) {
            billingDate = billingDate.plusMonths(1L);
        }

        int dayOfMonth = account.getMonthBillingDayAt();
        if (dayOfMonth > billingDate.lengthOfMonth()) {
            dayOfMonth = billingDate.lengthOfMonth();
        }
        return billingDate.withDayOfMonth(dayOfMonth);
    }

    /**
     * Calculates the following values based on the records within the period:
     *
     * <ol>
     *     <li>Total incomes.</li>
     *     <li>Total expenses.</li>
     *     <li>Expected final balance.</li>
     *     <li>Balance up to date.</li>
     * </ol>
     *
     * @param records List of records within the period being calculated.
     * @return {@link BalanceSummary} instance with the aforementioned values calculated and filled in.
     */
    protected BalanceSummary calculateBalanceSummary(final List<ExistingRecord> records) {
        BigDecimal totalIncomes = BigDecimal.valueOf(
            records.stream()
                .filter(record -> record.getRecordValue().compareTo(BigDecimal.ZERO) > 0)
                .map(ExistingRecord::getRecordValue)
                .mapToDouble(BigDecimal::doubleValue)
                .sum()
        );

        BigDecimal totalExpenses = BigDecimal.valueOf(
            records.stream()
                .filter(record -> record.getRecordValue().compareTo(BigDecimal.ZERO) < 0)
                .map(ExistingRecord::getRecordValue)
                .mapToDouble(BigDecimal::doubleValue)
                .sum()
        );

        LocalDate now = LocalDate.now();
        BigDecimal balanceUpToDate = BigDecimal.valueOf(
            records.stream()
                .filter(record -> record.getRecordDate().compareTo(now) <= 0)
                .map(ExistingRecord::getRecordValue)
                .mapToDouble(BigDecimal::doubleValue)
                .sum()
        );

        BigDecimal finalBalance = totalIncomes.add(totalExpenses);
        BalanceSummary summary = new BalanceSummary();
        summary.setTotalIncomes(totalIncomes);
        summary.setTotalExpenses(totalExpenses);
        summary.setFinalBalance(finalBalance);
        summary.setBalanceUpToDate(balanceUpToDate);
        return summary;
    }

    protected List<ExistingRecord> loadAccountRecords(ExistingAccount account, BalanceDates balanceDates) {
        List<ExistingRecord> records = this.recordService.loadAll(account.getId(), balanceDates.getPeriodStartDate(), balanceDates.getPeriodEndDate());
        if (account.getMainAccount()) {
            List<ExistingAccount> dependants = this.accountService.loadDependantAccounts();
            LocalDate refDate = balanceDates.getReferenceDate().minusMonths(1L);

            for(ExistingAccount dependantAccount: dependants) {
                Balance dependantAccountBalance = this.generateBalance(dependantAccount.getId(), refDate);
                if (dependantAccountBalance.getSummary().getFinalBalance().compareTo(BigDecimal.ZERO) != 0) {
                    records.add(this.dependantAccountRecordMapper.mapTo(dependantAccountBalance));
                }
            }

            records = records.stream()
                .sorted(Comparator.comparing(ExistingRecord::getRecordDate))
                .collect(Collectors.toList());
        }
        return records;
    }

}
