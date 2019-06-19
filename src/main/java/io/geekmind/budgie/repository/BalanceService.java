package io.geekmind.budgie.repository;

import io.geekmind.budgie.model.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BalanceService {

    private final AccountService accountService;
    private final RecordService recordService;

    @Autowired
    public BalanceService(AccountService accountService,
                          RecordService recordService) {
        this.accountService = accountService;
        this.recordService = recordService;
    }

    public Balance generateBalance(Integer accountId, LocalDate referenceDate) {
        Balance balance = new Balance();
        ExistingAccount existingAccount = this.retrieveAccount(accountId);
        if (null != existingAccount) {
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

    public ExistingAccount retrieveAccount(Integer accountId) {
        ExistingAccount account = null;
        if (null == accountId) {
            account = this.accountService.getMainAccount().orElse(null);
        } else {
            account = this.accountService.loadById(accountId).orElse(null);
        }
        return account;
    }

    public BalanceDates calculateBalanceDates(ExistingAccount account, LocalDate referenceDate) {
        LocalDate refDate = LocalDate.now();
        if (null != referenceDate) {
            refDate = referenceDate;
        }

        LocalDate periodEndDate = this.calculatePeriodEndDate(account, refDate);
        LocalDate periodStartDate = this.calculatePeriodStartDate(account, periodEndDate);
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

    public LocalDate calculatePeriodEndDate(ExistingAccount account, LocalDate referenceDate) {
        LocalDate periodEndDate = referenceDate;
        Integer dayOfMonth = 31;
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

    public LocalDate calculatePeriodStartDate(ExistingAccount account, LocalDate periodEndDate) {
        LocalDate periodStartDate = periodEndDate;
        int startDay = account.getMonthStartingAt();
        if (startDay > periodStartDate.lengthOfMonth()) {
            startDay = periodEndDate.lengthOfMonth();
        }

        if (startDay > periodEndDate.getDayOfMonth()) {
            periodStartDate = periodStartDate.minusMonths(1L);
        }

        return periodStartDate.withDayOfMonth(startDay);
    }

    public LocalDate calculatePeriodBillingDate(ExistingAccount account, LocalDate periodEndDate) {
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

    public BalanceSummary calculateBalanceSummary(List<ExistingRecord> records) {
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

    public List<ExistingRecord> loadAccountRecords(ExistingAccount account, BalanceDates balanceDates) {
        List<ExistingRecord> records = this.recordService.loadAll(account.getId(), balanceDates.getPeriodStartDate(), balanceDates.getPeriodEndDate());
        if (account.getMainAccount()) {
            List<ExistingAccount> dependants = this.accountService.loadDependantAccounts();
            LocalDate refDate = balanceDates.getReferenceDate().minusMonths(1L);
            ExistingCategory dependantAccountCategory = new ExistingCategory();
            dependantAccountCategory.setId(-1);
            dependantAccountCategory.setName("Account Balance");

            for(ExistingAccount dependantAccount: dependants) {
                Balance dependantAccountBalance = this.generateBalance(dependantAccount.getId(), refDate);
                if (dependantAccountBalance.getSummary().getFinalBalance().compareTo(BigDecimal.ZERO) != 0) {
                    ExistingRecord dependantAccountRecord = new ExistingRecord();
                    dependantAccountRecord.setId(-1);
                    dependantAccountRecord.setDescription(
                        String.format("Balance of %s between %s and %s.",
                            dependantAccount.getName(),
                            dependantAccountBalance.getBalanceDates().getPeriodStartDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                            dependantAccountBalance.getBalanceDates().getPeriodEndDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                        )
                    );
                    dependantAccountRecord.setRecordDate(dependantAccountBalance.getBalanceDates().getPeriodBillingDate());
                    dependantAccountRecord.setRecordValue(dependantAccountBalance.getSummary().getFinalBalance());
                    dependantAccountRecord.setAccount(dependantAccount);
                    dependantAccountRecord.setCategory(dependantAccountCategory);

                    records.add(dependantAccountRecord);
                }
            }
            records = records.stream()
                .sorted(Comparator.comparing(ExistingRecord::getRecordDate))
                .collect(Collectors.toList());
        }
        return records;
    }

}
