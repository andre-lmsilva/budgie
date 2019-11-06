package io.geekmind.budgie.model.mapper;

import io.geekmind.budgie.model.mapper.category.CategoryToEditCategoryMappingTest;
import io.geekmind.budgie.model.mapper.category.CategoryToExistingCategoryMappingTest;
import io.geekmind.budgie.model.mapper.category.CategoryToNewCategoryMappingTest;
import io.geekmind.budgie.model.mapper.category.EditCategoryToCategoryMergingTest;
import io.geekmind.budgie.model.mapper.category.ExistingCategoryToCategoryMappingTest;
import io.geekmind.budgie.model.mapper.category.NewCategoryToCategoryMappingTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)
@Suite.SuiteClasses({
        CategoryToExistingCategoryMappingTest.class,
        ExistingCategoryToCategoryMappingTest.class,
        NewCategoryToCategoryMappingTest.class,
        CategoryToNewCategoryMappingTest.class,
        CategoryToEditCategoryMappingTest.class,
        EditCategoryToCategoryMergingTest.class
})
public class CategoryMappingTest {

}
