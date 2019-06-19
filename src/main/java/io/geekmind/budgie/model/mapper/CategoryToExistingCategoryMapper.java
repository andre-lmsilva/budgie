package io.geekmind.budgie.model.mapper;

import io.geekmind.budgie.model.dto.ExistingCategory;
import io.geekmind.budgie.model.entity.Category;
import org.springframework.stereotype.Component;

@Component(CategoryToExistingCategoryMapper.QUALIFIER)
public class CategoryToExistingCategoryMapper implements Mapper<Category, ExistingCategory> {

    public static final String QUALIFIER = "categoryToExistingCategoryMapper";

    @Override
    public ExistingCategory mapTo(Category source) {
        ExistingCategory existingCategory = new ExistingCategory();
        existingCategory.setId(source.getId());
        existingCategory.setName(source.getName());
        existingCategory.setDescription(source.getDescription());
        return existingCategory;
    }

    @Override
    public Category mapFrom(ExistingCategory target) {
        throw new UnsupportedOperationException("There is no mapping operation in that direction.");
    }
}
