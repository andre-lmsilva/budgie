package io.geekmind.budgie.model.mapper;

import io.geekmind.budgie.fixture.CategoryFixture;
import io.geekmind.budgie.model.dto.ExistingCategory;
import io.geekmind.budgie.model.entity.Category;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class CategoryToExistingCategoryMapperTest {

    private Category categorySource;
    private ExistingCategory resultingExistingCategory;

    @Before
    public void setUp() {
        this.categorySource = CategoryFixture.get();
        this.resultingExistingCategory = new CategoryToExistingCategoryMapper().mapTo(this.categorySource);
    }

    @Test
    public void idIsMappedToExistingCategoryIdAttribute() {
        assertThat(resultingExistingCategory)
            .hasFieldOrPropertyWithValue("id", this.categorySource.getId());
    }

    @Test
    public void nameIsMappedToExistingCategoryNameAttribute() {
        assertThat(resultingExistingCategory)
            .hasFieldOrPropertyWithValue("name", this.resultingExistingCategory.getName());
    }

    @Test
    public void descriptionIsMappedToExistingCategoryDescriptionAttribute() {
        assertThat(resultingExistingCategory)
            .hasFieldOrPropertyWithValue("description", this.resultingExistingCategory.getDescription());
    }
}