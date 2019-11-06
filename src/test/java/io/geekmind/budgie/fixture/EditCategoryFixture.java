package io.geekmind.budgie.fixture;

import io.geekmind.budgie.model.dto.category.EditCategory;

import java.math.BigDecimal;

public class EditCategoryFixture {

    public static EditCategory foodExpenses() {
        EditCategory category = new EditCategory();
        category.setId(-999);
        category.setName("Updated Food Expenses Fake Category");
        category.setDescription("Updated fake category.");
        category.setMaxExpenses(BigDecimal.valueOf(543.21D));
        return category;
    }

}
