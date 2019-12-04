package io.geekmind.budgie.model.mapper;

import io.geekmind.budgie.model.mapper.budget_template_record_container.BudgetTemplateRecordContainerToNewBudgetTemplateRecordMappingTest;
import io.geekmind.budgie.model.mapper.budget_template_record_container.NewBudgetTemplateRecordToBudgetTemplateRecordContainerMappingTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    NewBudgetTemplateRecordToBudgetTemplateRecordContainerMappingTest.class,
    BudgetTemplateRecordContainerToNewBudgetTemplateRecordMappingTest.class
})
public class BudgetTemplateRecordContainerMappingTest {
}
