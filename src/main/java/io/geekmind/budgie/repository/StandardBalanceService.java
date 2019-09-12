package io.geekmind.budgie.repository;

import io.geekmind.budgie.model.dto.*;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Predicate;
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

    /**
     * Provides a predicate that will filter a list of {@link ExistingRecord} returning only the records with a positive
     * record value.
     */
    public static final Predicate<ExistingRecord> INCOMES_RECORDS_FILTER = (existingRecord ->
        null != existingRecord.getRecordValue() && existingRecord.getRecordValue().compareTo(BigDecimal.ZERO) > 0
    );

    /**
     * Provides a predicate that will filter a list of {@link ExistingRecord} returning only the record with a negative
     * record value.
     */
    public static final Predicate<ExistingRecord> EXPENSES_RECORDS_FILTER = (existingRecord ->
        null != existingRecord.getRecordValue() && existingRecord.getRecordValue().compareTo(BigDecimal.ZERO) < 0
    );

    /**
     * Provides a predicate that will filter a list of {@link ExistingRecord} returning any record which record value is
     * not null.
     */
    public static final Predicate<ExistingRecord> ANY_RECORDS_FILTER = (existingRecord -> null != existingRecord.getRecordValue());

    private final MapperFacade mapper;

    @Autowired
    public StandardBalanceService(AccountService accountService,
                                  RecordService recordService,
                                  MapperFacade mapper) {
        this.accountService = accountService;
        this.recordService = recordService;
        this.mapper = mapper;
    }

    /**
     * Calculates the balance of an account based on a date of reference. This method acts as a facade, delegating to
     * the other class methods the real task to calculate the values.
     *
     * @param accountId         Id of the account which balance is being calculated.
     * @param referenceDate     Period reference date. It is optional. The current date will be assume when null.
     * @return
     */
    public Balance generateBalance(Integer accountId, LocalDate referenceDate) {
        Balance balance = new Balance();
        Optional<ExistingAccount> retrievedAccount = this.retrieveAccount(accountId);

        if (retrievedAccount.isPresent()) {
            ExistingAccount existingAccount = retrievedAccount.get();
            BalanceDates balanceDates = this.calculateBalanceDates(existingAccount, referenceDate);
            List<ExistingRecord> records = this.loadAccountRecords(existingAccount, balanceDates);
            BalanceSummary balanceSummary = this.calculateBalanceSummary(
                records,
                balanceDates.getPeriodStartDate(),
                balanceDates.getPeriodEndDate()
            );
            List<CategoryBalanceSummary> categoryBalanceSummaries = this.calculateCategoryBalanceSummary(
                records,
                balanceSummary
            );

            balance = new Balance(existingAccount, balanceDates, records, balanceSummary, categoryBalanceSummaries);
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
        LocalDate previousPeriodStartDate = refDate.minusMonths(1L);
        LocalDate nextPeriodStartDate = refDate.plusMonths(1L);
        Integer periodRemainingDays = this.calculatePeriodRemainingDays(periodEndDate);


        return  new BalanceDates(
            refDate,
            periodStartDate,
            periodEndDate,
            periodBillingDate,
            previousPeriodStartDate,
            nextPeriodStartDate,
            periodRemainingDays
        );
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
     * Calculates the remaining days from the current date until the period end date.
     * If the end of the period is in the past, it returns a negative number.
     *
     * @param periodEndDate Periods end date.
     * @return Number of the days from today until the end of the period.
     */
    protected Integer calculatePeriodRemainingDays(LocalDate periodEndDate) {
        return ((Long)ChronoUnit.DAYS.between(LocalDate.now(), periodEndDate)).intValue();
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
    protected BalanceSummary calculateBalanceSummary(final List<ExistingRecord> records,
                                                     final LocalDate periodStartDate,
                                                     final LocalDate periodEndDate) {

        Predicate<ExistingRecord> fullPeriodFilter = this.periodIntervalFilter(periodStartDate, periodEndDate);
        Predicate<ExistingRecord> upToDateFilter = this.periodIntervalFilter(periodStartDate, LocalDate.now());

        BigDecimal totalIncomes = this.sumarizeRecords(records, INCOMES_RECORDS_FILTER, fullPeriodFilter);
        BigDecimal totalExpenses = this.sumarizeRecords(records, EXPENSES_RECORDS_FILTER, fullPeriodFilter);
        BigDecimal balanceUpToDate = this.sumarizeRecords(records, ANY_RECORDS_FILTER, upToDateFilter);
        BigDecimal finalBalance = this.sumarizeRecords(records, ANY_RECORDS_FILTER, fullPeriodFilter);

        return new BalanceSummary(finalBalance, totalIncomes, totalExpenses, balanceUpToDate);
    }

    /**
     * Calculates the total income value for the period.
     * @param records   Record of the period.
     * @return Total income for the period
     */
    protected BigDecimal sumarizeRecords(final List<ExistingRecord> records,
                                         final Predicate<ExistingRecord> valueFilter,
                                         final Predicate<ExistingRecord> periodFilter) {
        return BigDecimal.valueOf(
            records.stream()
                .filter(periodFilter)
                .filter(valueFilter)
                .map(ExistingRecord::getRecordValue)
                .mapToDouble(BigDecimal::doubleValue)
                .sum()
        );
    }

    /**
     * Loads the records within a period for an {@link ExistingAccount}. If it is the main account, the dependant
     * accounts balances will also be calculated and records with be added to the list in the proper billing date. The
     * final list is sorted by record date in an ascending order.
     *
     * @param account       Account to have its records retrieved.
     * @param balanceDates  {@link BalanceDates} with the period dates already calculated.
     * @return  List containing all the account records within the received period and the balances of the dependant
     *          accounts if the account is the main account.
     */
    protected List<ExistingRecord> loadAccountRecords(ExistingAccount account, BalanceDates balanceDates) {
        List<ExistingRecord> records = this.recordService.loadAll(account.getId(), balanceDates.getPeriodStartDate(), balanceDates.getPeriodEndDate());
        if (account.getMainAccount()) {
            records.addAll(this.calculateDependantAccountBalances(balanceDates));
            records = records.stream()
                .sorted(Comparator.comparing(ExistingRecord::getRecordDate))
                .collect(Collectors.toList());
        }
        return records;
    }

    /**
     * Calculates the balance of the main account dependant accounts and returns in a list of
     * unsorted records.
     *
     * @param mainAccountBalanceDates   Main account calculated period dates.
     * @return List containing unsorted record with the dependant account balances.
     */
    protected List<ExistingRecord> calculateDependantAccountBalances(BalanceDates mainAccountBalanceDates) {
        List<ExistingRecord> dependantAccountBalances = new ArrayList<>();
        List<ExistingAccount> dependantAccounts = this.accountService.loadDependantAccounts();
        LocalDate referenceDate = mainAccountBalanceDates.getReferenceDate().minusMonths(1L);

        for(ExistingAccount dependantAccount: dependantAccounts) {
            Balance dependantAccountBalance = this.generateBalance(dependantAccount.getId(), referenceDate);
            ExistingRecord dependantAccountBalanceRecord = this.mapper.map(dependantAccountBalance, DependantAccountRecord.class);
            dependantAccountBalances.add(dependantAccountBalanceRecord);
        }

        return dependantAccountBalances.stream()
            .filter(record -> record.getRecordValue().compareTo(BigDecimal.ZERO) != 0)
            .collect(Collectors.toList());
    }

    /**
     * Returns the {@link Predicate} capable to filter {@link ExistingRecord} instances which record date is within
     * a period of time expressed by the received start and end date.
     * @param startDate Period start date.
     * @param endDate   Period end date.
     * @return A predicate that can be used to filter {@link ExistingRecord} within a period of time.
     */
    protected Predicate<ExistingRecord> periodIntervalFilter(LocalDate startDate, LocalDate endDate) {
        return existingRecord ->
            existingRecord.getRecordDate().compareTo(startDate) >= 0 &&
            existingRecord.getRecordDate().compareTo(endDate) <= 0;
    }

    /**
     * Summarizes the balance per category, considering only debit records.
     * @param records           Records within the period.
     * @param balanceSummary    Balance calculated summary.
     * @return List of {@link CategoryBalanceSummary} containing the summarized balance per category and sorted by
     *         {@link CategoryBalanceSummary#getExpensesConsumptionPercentage} value.
     */
    protected List<CategoryBalanceSummary> calculateCategoryBalanceSummary(final List<ExistingRecord> records,
                                                                           final BalanceSummary balanceSummary) {
        Map<Integer, CategoryBalanceSummary> categorySummaries = new HashMap<>();
        for(ExistingRecord record: records) {
            if (record.getRecordValue().compareTo(BigDecimal.ZERO) < 0) {
                CategoryBalanceSummary categoryBalanceSummary = categorySummaries.computeIfAbsent(
                        record.getCategory().getId(),
                        categoryId -> new CategoryBalanceSummary(record.getCategory(), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO)
                );

                BigDecimal newBalance = categoryBalanceSummary.getBalance().add(record.getRecordValue());
                BigDecimal newExpensesConsumptionPercentage = newBalance.divide(
                        balanceSummary.getTotalExpenses(),
                        RoundingMode.HALF_UP
                ).multiply(BigDecimal.valueOf(100D));

                BigDecimal maxExpenses = BigDecimal.ZERO;
                if (null != record.getCategory().getMaxExpenses()) {
                    maxExpenses = record.getCategory().getMaxExpenses().negate();
                }

                BigDecimal maxExpensesConsumption = BigDecimal.ZERO;
                if (null != record.getCategory().getMaxExpenses() &&
                    record.getCategory().getMaxExpenses().compareTo(BigDecimal.ZERO) != 0) {
                    maxExpensesConsumption = newBalance
                        .divide(
                            record.getCategory().getMaxExpenses(),
                            RoundingMode.HALF_UP
                        ).multiply(BigDecimal.valueOf(100D));
                }

                categoryBalanceSummary.setBalance(newBalance);
                categoryBalanceSummary.setExpensesConsumptionPercentage(newExpensesConsumptionPercentage);
                categoryBalanceSummary.setMaxExpensesConsumption(maxExpensesConsumption);
                categoryBalanceSummary.setMaxExpenses(maxExpenses);
            }
        }

        return categorySummaries.values()
            .stream()
            .sorted(Comparator.comparing(CategoryBalanceSummary::getBalance))
            .collect(Collectors.toList());
    }
}
