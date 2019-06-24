package io.geekmind.budgie.fixture;

import io.geekmind.budgie.model.dto.NewCategory;

public class NewCategoryFixture {

    public static NewCategory get() {
        return new NewCategory("Fake Category", "Just for test purpose.");
    }
}
