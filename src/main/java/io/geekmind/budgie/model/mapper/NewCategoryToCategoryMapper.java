package io.geekmind.budgie.model.mapper;

import io.geekmind.budgie.model.dto.NewCategory;
import io.geekmind.budgie.model.entity.Category;
import org.springframework.stereotype.Component;

/**
 * Maps attribute from {@link NewCategory} to attributes of a new instance of {@link Category}.
 *
 * @author Andre Silva
 */
@Component(NewCategoryToCategoryMapper.QUALIFIER)
public class NewCategoryToCategoryMapper implements Mapper<NewCategory, Category> {

    public static final String QUALIFIER = "newCategoryToCategoryMapper";

    @Override
    public Category mapTo(NewCategory source) {
        Category category = new Category();
        category.setName(source.getName());
        category.setDescription(source.getDescription());
        return category;
    }

    @Override
    public NewCategory mapFrom(Category target) {
        throw new UnsupportedOperationException("There is no mapping in this direction.");
    }
}
