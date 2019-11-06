package io.geekmind.budgie.model.mapper.category;

import io.geekmind.budgie.fixture.ExistingCategoryFixture;
import io.geekmind.budgie.model.dto.category.ExistingCategory;
import io.geekmind.budgie.model.entity.Category;
import io.geekmind.budgie.model.mapper.CategoryMappingSettings;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ExistingCategoryToCategoryMappingTest {

    private ExistingCategory sourceExistingCategory;
    private Category resultCategory;

    @Before
    public void setUp() {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        new CategoryMappingSettings().configure(mapperFactory);
        this.sourceExistingCategory = ExistingCategoryFixture.getFuel();
        this.resultCategory = mapperFactory.getMapperFacade().map(this.sourceExistingCategory, Category.class);
    }

    @Test
    public void allAttributes_areNotMapped() {
        assertThat(this.resultCategory)
            .hasFieldOrPropertyWithValue("id", null)
            .hasFieldOrPropertyWithValue("name", null)
            .hasFieldOrPropertyWithValue("description", null)
            .hasFieldOrPropertyWithValue("maxExpenses", null);
    }

}
