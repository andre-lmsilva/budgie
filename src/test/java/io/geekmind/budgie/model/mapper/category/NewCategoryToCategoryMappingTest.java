package io.geekmind.budgie.model.mapper.category;

import io.geekmind.budgie.fixture.NewCategoryFixture;
import io.geekmind.budgie.model.dto.category.NewCategory;
import io.geekmind.budgie.model.entity.Category;
import io.geekmind.budgie.model.mapper.CategoryMappingSettings;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class NewCategoryToCategoryMappingTest {

    private NewCategory sourceNewCategory;
    private Category resultCategory;

    @Before
    public void setUp() {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        new CategoryMappingSettings().configure(mapperFactory);
        this.sourceNewCategory = NewCategoryFixture.get();
        this.resultCategory = mapperFactory.getMapperFacade().map(this.sourceNewCategory, Category.class);
    }

    @Test
    public void idAttribute_isNotMapped() {
        assertThat(this.resultCategory)
            .hasFieldOrPropertyWithValue("id", null);
    }

    @Test
    public void nameAttribute_isMapped() {
        assertThat(this.resultCategory)
            .hasFieldOrPropertyWithValue("name", this.sourceNewCategory.getName());
    }

    @Test
    public void descriptionAttribute_isMapped() {
        assertThat(this.resultCategory)
            .hasFieldOrPropertyWithValue("description", this.sourceNewCategory.getDescription());
    }

    @Test
    public void maxExpensesAttribute_isMapped() {
        assertThat(this.resultCategory)
            .hasFieldOrPropertyWithValue("maxExpenses", this.sourceNewCategory.getMaxExpenses());
    }

}
