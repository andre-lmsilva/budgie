package io.geekmind.budgie.model.mapper;

import io.geekmind.budgie.fixture.NewCategoryFixture;
import io.geekmind.budgie.model.dto.NewCategory;
import io.geekmind.budgie.model.entity.Category;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class NewCategoryToCategoryMapperTest {

    private NewCategory newCategorySource;
    private Category resultingCategory;

    @Before
    public void setUp() {
        this.newCategorySource = NewCategoryFixture.get();
        this.resultingCategory = new NewCategoryToCategoryMapper().mapTo(this.newCategorySource);
    }

    @Test
    public void idIsAlwaysNull() {
        assertThat(this.resultingCategory.getId()).isNull();
    }

    @Test
    public void nameIsMappedToCategoryNameAttribute() {
        assertThat(this.resultingCategory)
            .hasFieldOrPropertyWithValue("name", this.newCategorySource.getName());
    }

    @Test
    public void descriptionIsMappedToCategoryDescriptionAttribute() {
        assertThat(this.resultingCategory)
            .hasFieldOrPropertyWithValue("description", this.newCategorySource.getDescription());
    }


}