package io.geekmind.budgie.model.mapper;

import io.geekmind.budgie.model.dto.NewSingleRecord;
import io.geekmind.budgie.model.entity.SingleRecord;
import ma.glasnost.orika.MapperFactory;
import net.rakugakibox.spring.boot.orika.OrikaMapperFactoryConfigurer;
import org.springframework.stereotype.Component;

@Component
public class SingleRecordMappingSettings implements OrikaMapperFactoryConfigurer {
    @Override
    public void configure(MapperFactory orikaMapperFactory) {
        orikaMapperFactory.classMap(NewSingleRecord.class, SingleRecord.class)
            .field("accountId", "account.id")
            .field("categoryId", "category.id")
            .byDefault()
            .register();
    }
}
