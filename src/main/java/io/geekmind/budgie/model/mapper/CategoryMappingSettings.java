package io.geekmind.budgie.model.mapper;

import io.geekmind.budgie.model.dto.category.ExistingCategory;
import io.geekmind.budgie.model.dto.category.EditCategory;
import io.geekmind.budgie.model.dto.category.NewCategory;
import io.geekmind.budgie.model.entity.Category;
import ma.glasnost.orika.MapperFactory;
import net.rakugakibox.spring.boot.orika.OrikaMapperFactoryConfigurer;
import org.springframework.stereotype.Component;

@Component
public class CategoryMappingSettings implements OrikaMapperFactoryConfigurer {
    @Override
    public void configure(MapperFactory orikaMapperFactory) {
        orikaMapperFactory.classMap(Category.class, ExistingCategory.class)
            .fieldAToB("id", "id")
            .fieldAToB("name", "name")
            .fieldAToB("description", "description")
            .fieldAToB("maxExpenses", "maxExpenses")
            .register();

        orikaMapperFactory.classMap(NewCategory.class, Category.class)
            .fieldAToB("name", "name")
            .fieldAToB("description", "description")
            .fieldAToB("maxExpenses", "maxExpenses")
            .register();

        orikaMapperFactory.classMap(EditCategory.class, Category.class)
            .fieldBToA("id", "id")
            .byDefault()
            .register();


    }
}
