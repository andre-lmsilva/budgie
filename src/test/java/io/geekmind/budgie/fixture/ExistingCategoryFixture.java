package io.geekmind.budgie.fixture;

import io.geekmind.budgie.model.dto.ExistingCategory;

public class ExistingCategoryFixture {

    public static ExistingCategory get() {
        return new ExistingCategory(-1, "Fake category", "Fake category used exclusively for test purposes.");
    }

}