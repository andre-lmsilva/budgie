package io.geekmind.budgie.repository;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    StandardBalanceServiceRetrieveAccountTest.class,
    StandardBalanceServiceCalculatePeriodEndDateTest.class,
    StandardBalanceServiceCalculatePeriodStartDateTest.class,
    StandardBalanceServiceCalculatePeriodBillingDateTest.class,
    StandardBalanceServiceCalculateBalanceSummaryTest.class,
    StandardBalanceServiceCalculateBalanceDatesTest.class,
    StandardBalanceServicePeriodIntervalFilterTest.class,
    StandardBalanceServiceFiltersTest.class,
    StandardBalanceServiceCalculateDependantAccountBalancesTest.class,
    StandardBalanceServiceLoadAccountRecordsTest.class,
    StandardBalanceServiceGenerateBalanceTest.class
})
public class StandardBalanceServiceTest {

}