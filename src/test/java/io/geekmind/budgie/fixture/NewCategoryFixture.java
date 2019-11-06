package io.geekmind.budgie.fixture;

import io.geekmind.budgie.model.dto.category.NewCategory;

import java.math.BigDecimal;


public class NewCategoryFixture {

    public static NewCategory get() {
        NewCategory newCategory = new NewCategory();
        newCategory.setName("Fake Category");
        newCategory.setDescription("Just a fake category meant to be used by tests.");
        newCategory.setMaxExpenses(BigDecimal.TEN);
        return newCategory;
    }
}
