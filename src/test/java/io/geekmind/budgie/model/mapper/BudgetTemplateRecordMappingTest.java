package io.geekmind.budgie.model.mapper;

import io.geekmind.budgie.model.mapper.budget_template_record.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    NewBudgetTemplateRecordToBudgetTemplateRecordMappingTest.class,
    BudgetTemplateRecordToNewBudgetTemplateRecordMappingTest.class,
    BudgetTemplateRecordToExistingRecordMappingTest.class,
    ExistingRecordToBudgetTemplateRecordMappingTest.class
})
public class BudgetTemplateRecordMappingTest {
}
