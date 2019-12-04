package io.geekmind.budgie.model.mapper;

import io.geekmind.budgie.model.mapper.budget_record.BudgetRecordToExistingRecordMappingTest;
import io.geekmind.budgie.model.mapper.budget_record.ExistingRecordToBudgetRecordMappingTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    BudgetRecordToExistingRecordMappingTest.class,
    ExistingRecordToBudgetRecordMappingTest.class
})
public class BudgetRecordMappingTest {
}
