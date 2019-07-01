package io.geekmind.budgie.model.mapper;

import io.geekmind.budgie.model.dto.NewTransfer;
import io.geekmind.budgie.model.entity.TransferContainer;
import ma.glasnost.orika.MapperFactory;
import net.rakugakibox.spring.boot.orika.OrikaMapperFactoryConfigurer;
import org.springframework.stereotype.Component;

@Component
public class TransferContainerMappingSettings implements OrikaMapperFactoryConfigurer {

    @Override
    public void configure(MapperFactory orikaMapperFactory) {
        orikaMapperFactory.classMap(NewTransfer.class, TransferContainer.class)
            .field("description", "name")
            .byDefault()
            .register();
    }
}
