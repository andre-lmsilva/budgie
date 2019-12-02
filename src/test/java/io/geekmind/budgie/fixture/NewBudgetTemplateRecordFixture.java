package io.geekmind.budgie.fixture;

import io.geekmind.budgie.model.dto.budget_template_record.NewBudgetTemplateRecord;

import java.math.BigDecimal;

public class NewBudgetTemplateRecordFixture {

    public static NewBudgetTemplateRecord salary() {
        NewBudgetTemplateRecord newBudgetTemplateRecord = new NewBudgetTemplateRecord();
        newBudgetTemplateRecord.setDayOfMonth(28);
        newBudgetTemplateRecord.setAccountId(-1);
        newBudgetTemplateRecord.setCategoryId(-2);
        newBudgetTemplateRecord.setDescription("Monthly Salary");
        newBudgetTemplateRecord.setRecordValue(BigDecimal.valueOf(4000D));
        return newBudgetTemplateRecord;
    }
}
