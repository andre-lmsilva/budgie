package io.geekmind.budgie.model.mapper.category;

import io.geekmind.budgie.fixture.CategoryFixture;
import io.geekmind.budgie.model.dto.category.EditCategory;
import io.geekmind.budgie.model.entity.Category;
import io.geekmind.budgie.model.mapper.CategoryMappingSettings;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CategoryToEditCategoryMappingTest {

    private Category sourceCategory;
    private EditCategory resultEditCategory;

    @Before
    public void setUp() {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        new CategoryMappingSettings().configure(mapperFactory);
        this.sourceCategory = CategoryFixture.get();
        this.resultEditCategory = mapperFactory.getMapperFacade().map(this.sourceCategory, EditCategory.class);
    }

    @Test
    public void idAttribute_isMapped() {
        assertThat(this.resultEditCategory)
            .hasFieldOrPropertyWithValue("id", this.sourceCategory.getId());
    }

    @Test
    public void nameAttribute_isMapped() {
        assertThat(this.resultEditCategory)
            .hasFieldOrPropertyWithValue("name", this.sourceCategory.getName());
    }

    @Test
    public void descriptionAttribute_isMapped() {
        assertThat(this.resultEditCategory)
            .hasFieldOrPropertyWithValue("description", this.sourceCategory.getDescription());
    }

    @Test
    public void maxExpensesAttribute_isMapped() {
        assertThat(this.resultEditCategory)
            .hasFieldOrPropertyWithValue("maxExpenses", this.sourceCategory.getMaxExpenses());
    }

}
