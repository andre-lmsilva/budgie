package io.geekmind.budgie.fixture;

import io.geekmind.budgie.model.dto.NewCategory;

import java.math.BigDecimal;

public class NewCategoryFixture {

    public static NewCategory get() {
        return new NewCategory("Fake Category", "Just for test purpose.", BigDecimal.valueOf(-123.4D));
    }
}
