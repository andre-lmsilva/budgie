package io.geekmind.budgie.model.mapper;

import io.geekmind.budgie.model.dto.ExistingCategory;
import io.geekmind.budgie.model.entity.Category;
import ma.glasnost.orika.MapperFactory;
import net.rakugakibox.spring.boot.orika.OrikaMapperFactoryConfigurer;
import org.springframework.stereotype.Component;

@Component
public class CategoryMappingSettings implements OrikaMapperFactoryConfigurer {
    @Override
    public void configure(MapperFactory orikaMapperFactory) {
        orikaMapperFactory.classMap(Category.class, ExistingCategory.class)
            .mapNulls(false)
            .mapNullsInReverse(false)
            .byDefault()
            .register();
    }
}
