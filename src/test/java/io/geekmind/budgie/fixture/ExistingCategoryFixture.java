package io.geekmind.budgie.fixture;

import io.geekmind.budgie.model.dto.category.ExistingCategory;

import java.math.BigDecimal;

public class ExistingCategoryFixture {

    public static ExistingCategory get() {
        ExistingCategory category = new ExistingCategory();
        category.setId(-1);
        category.setName("Fake Category");
        category.setDescription("Fake category used exclusively for test purpose.");
        category.setMaxExpenses(BigDecimal.ZERO);
        return category;
    }

    public static ExistingCategory getFuel() {
        ExistingCategory category = new ExistingCategory();
        category.setId(-2);
        category.setName("Fake Fuel");
        category.setDescription("Fake fuel expenses category.");
        category.setMaxExpenses(BigDecimal.ZERO);
        return category;
    }
}
