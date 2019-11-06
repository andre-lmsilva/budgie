package io.geekmind.budgie.model.mapper.category;

import io.geekmind.budgie.fixture.CategoryFixture;
import io.geekmind.budgie.model.dto.category.NewCategory;
import io.geekmind.budgie.model.entity.Category;
import io.geekmind.budgie.model.mapper.CategoryMappingSettings;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CategoryToNewCategoryMappingTest {

    private Category sourceCategory;
    private NewCategory resultNewCategory;

    @Before
    public void setUp() {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        new CategoryMappingSettings().configure(mapperFactory);
        this.sourceCategory = CategoryFixture.get();
        this.resultNewCategory = mapperFactory.getMapperFacade().map(this.sourceCategory, NewCategory.class);
    }

    @Test
    public void allAttributes_areNotMapped() {
        assertThat(this.resultNewCategory)
            .hasFieldOrPropertyWithValue("name", null)
            .hasFieldOrPropertyWithValue("description", null)
            .hasFieldOrPropertyWithValue("maxExpenses", null);
    }
}
