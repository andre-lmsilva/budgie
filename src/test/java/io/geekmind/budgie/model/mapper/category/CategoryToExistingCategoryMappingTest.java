package io.geekmind.budgie.model.mapper.category;

import io.geekmind.budgie.fixture.CategoryFixture;
import io.geekmind.budgie.model.dto.category.ExistingCategory;
import io.geekmind.budgie.model.entity.Category;
import io.geekmind.budgie.model.mapper.CategoryMappingSettings;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CategoryToExistingCategoryMappingTest {

    private Category sourceCategory;
    private ExistingCategory resultExistingCategory;

    @Before
    public void setUp() {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        new CategoryMappingSettings().configure(mapperFactory);
        this.sourceCategory = CategoryFixture.get();
        this.resultExistingCategory = mapperFactory.getMapperFacade().map(this.sourceCategory, ExistingCategory.class);
    }

    @Test
    public void idAttribute_isMapped() {
        assertThat(this.resultExistingCategory)
            .hasFieldOrPropertyWithValue("id", this.sourceCategory.getId());
    }

    @Test
    public void nameAttribute_isMapped() {
        assertThat(this.resultExistingCategory)
            .hasFieldOrPropertyWithValue("name", this.sourceCategory.getName());
    }

    @Test
    public void descriptionAttribute_isMapped() {
        assertThat(this.resultExistingCategory)
            .hasFieldOrPropertyWithValue("description", this.sourceCategory.getDescription());
    }

    @Test
    public void maxExpensesAttribute_isMapped() {
        assertThat(this.resultExistingCategory)
            .hasFieldOrPropertyWithValue("maxExpenses", this.sourceCategory.getMaxExpenses());
    }
}
