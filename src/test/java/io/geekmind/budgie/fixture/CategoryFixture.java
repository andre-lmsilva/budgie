package io.geekmind.budgie.fixture;

import io.geekmind.budgie.model.entity.Category;

public class CategoryFixture {

    public static Category get() {
        Category category = new Category();
        category.setId(-1);
        category.setName("Fake Category");
        category.setDescription("To be used exclusively for test purpose.");
        return category;
    }

}
