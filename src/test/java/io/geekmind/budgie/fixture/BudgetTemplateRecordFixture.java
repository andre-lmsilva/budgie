package io.geekmind.budgie.fixture;

import io.geekmind.budgie.model.entity.BudgetTemplateRecord;
import io.geekmind.budgie.model.entity.BudgetTemplateRecordContainer;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BudgetTemplateRecordFixture {

    public static BudgetTemplateRecord rent() {
        BudgetTemplateRecord budgetTemplateRecord = new BudgetTemplateRecord();
        budgetTemplateRecord.setId(-1);
        budgetTemplateRecord.setRecordDate(LocalDate.now().withMonth(12).withYear(9999));
        budgetTemplateRecord.setAccount(StandardAccountFixture.getMainAccount());
        budgetTemplateRecord.setCategory(CategoryFixture.get());
        budgetTemplateRecord.setDescription("Monthly Rent");
        budgetTemplateRecord.setRecordValue(BigDecimal.valueOf(1234.56D).negate());

        BudgetTemplateRecordContainer container = new BudgetTemplateRecordContainer();
        container.setId(-9);
        container.setName("Fake Budget Container");
        container.addRecord(budgetTemplateRecord);

        return budgetTemplateRecord;
    }

}
