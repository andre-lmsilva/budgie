package io.geekmind.budgie.fixture;

import io.geekmind.budgie.model.entity.BudgetRecord;
import io.geekmind.budgie.model.entity.BudgetTemplateRecordContainer;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BudgetRecordFixture {

    public static BudgetRecord salary() {
        BudgetRecord budgetRecord = new BudgetRecord();
        BudgetTemplateRecordContainer container = new BudgetTemplateRecordContainer();
        container.setId(-9);
        container.addRecord(budgetRecord);
        budgetRecord.setId(-1);
        budgetRecord.setAccount(StandardAccountFixture.getMainAccount());
        budgetRecord.setCategory(CategoryFixture.get());
        budgetRecord.setRecordDate(LocalDate.now());
        budgetRecord.setDescription("Monthly Salary");
        budgetRecord.setRecordValue(BigDecimal.valueOf(4000D));
        return budgetRecord;
    }

}
