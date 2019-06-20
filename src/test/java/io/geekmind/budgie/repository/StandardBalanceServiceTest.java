package io.geekmind.budgie.repository;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    StandardBalanceServiceRetrieveAccountTest.class,
    StandardBalanceServiceCalculatePeriodEndDateTest.class,
    StandardBalanceServiceCalculatePeriodStartDateTest.class,
    StandardBalanceServiceCalculateBalanceSummaryTest.class,
    StandardBalanceServiceCalculateBalanceDatesTest.class
})
public class StandardBalanceServiceTest {

}