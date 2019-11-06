package io.geekmind.budgie.model.mapper.category;

import io.geekmind.budgie.fixture.CategoryFixture;
import io.geekmind.budgie.fixture.EditCategoryFixture;
import io.geekmind.budgie.model.dto.category.EditCategory;
import io.geekmind.budgie.model.entity.Category;
import io.geekmind.budgie.model.mapper.CategoryMappingSettings;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EditCategoryToCategoryMergingTest {

    private EditCategory sourceEditCategory;
    private Category sourceCategory;
    private Category resultCategory;

    @Before
    public void setUp() {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        new CategoryMappingSettings().configure(mapperFactory);
        this.sourceEditCategory = EditCategoryFixture.foodExpenses();
        this.sourceCategory = CategoryFixture.get();
        this.resultCategory = CategoryFixture.get();
        mapperFactory.getMapperFacade().map(this.resultCategory, this.sourceEditCategory);
    }

    @Test
    public void idAttribute_isNotMapped() {
        assertThat(this.resultCategory)
            .hasFieldOrPropertyWithValue("id", this.sourceCategory.getId());
    }

    @Test
    public void nameAttribute_isMapped() {
        assertThat(this.resultCategory)
            .hasFieldOrPropertyWithValue("name", this.sourceEditCategory.getName());
    }

    @Test
    public void descriptionAttribute_isMapped() {
        assertThat(this.resultCategory)
            .hasFieldOrPropertyWithValue("description", this.sourceEditCategory.getDescription());
    }

    @Test
    public void maxExpensesAttribute_isMapped() {
        assertThat(this.resultCategory)
            .hasFieldOrPropertyWithValue("maxExpenses", this.sourceEditCategory.getMaxExpenses());
    }
}
