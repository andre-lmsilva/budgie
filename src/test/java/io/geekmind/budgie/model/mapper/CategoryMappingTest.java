package io.geekmind.budgie.model.mapper;

import io.geekmind.budgie.fixture.CategoryFixture;
import io.geekmind.budgie.fixture.ExistingCategoryFixture;
import io.geekmind.budgie.fixture.NewCategoryFixture;
import io.geekmind.budgie.model.dto.ExistingCategory;
import io.geekmind.budgie.model.dto.NewCategory;
import io.geekmind.budgie.model.entity.Category;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CategoryMappingTest {

    private MapperFacade mapper;

    @Before
    public void setUp() {
        MapperFactory factory = new DefaultMapperFactory.Builder().build();
        new CategoryMappingSettings().configure(factory);
        this.mapper = factory.getMapperFacade();
    }

    @Test
    public void mapping_FromCategoryToExistingCategory_MapsAllFields() {
        Category fakeCategory = CategoryFixture.get();
        ExistingCategory result = this.mapper.map(fakeCategory, ExistingCategory.class);
        assertThat(result).isEqualToComparingFieldByField(fakeCategory);
    }

    @Test
    public void mapping_FromNewCategoryToCategory_MapsAllFieldsExceptIdAttribute() {
        NewCategory fakeNewCategory = NewCategoryFixture.get();
        Category result = this.mapper.map(fakeNewCategory, Category.class);
        assertThat(result)
            .hasFieldOrPropertyWithValue("id", null)
            .hasFieldOrPropertyWithValue("name", fakeNewCategory.getName())
            .hasFieldOrPropertyWithValue("description", fakeNewCategory.getDescription());
    }

    @Test
    public void merging_FromExistingCategoryToAlreadyFilledCategoryInstance_DoesNotMapNullValues() {
        ExistingCategory fakeExistingCategory = ExistingCategoryFixture.get();
        fakeExistingCategory.setDescription(null);
        Category fakeCategory = CategoryFixture.get();

        this.mapper.map(fakeExistingCategory, fakeCategory);

        assertThat(fakeCategory)
            .hasFieldOrPropertyWithValue("id", fakeExistingCategory.getId())
            .hasFieldOrPropertyWithValue("name", fakeExistingCategory.getName())
            .hasFieldOrProperty("description");

    }

}
