package io.geekmind.budgie.model.mapper;

import io.geekmind.budgie.model.dto.NewInstalment;
import io.geekmind.budgie.model.entity.InstalmentContainer;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import net.rakugakibox.spring.boot.orika.OrikaMapperFactoryConfigurer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class InstalmentContainerMappingSettings implements OrikaMapperFactoryConfigurer {
    @Override
    public void configure(MapperFactory orikaMapperFactory) {
        orikaMapperFactory.classMap(NewInstalment.class, InstalmentContainer.class)
            . byDefault()
            .customize(new CustomMapper<NewInstalment, InstalmentContainer>() {
                @Override
                public void mapAtoB(NewInstalment newInstalment, InstalmentContainer instalmentContainer, MappingContext context) {
                    super.mapAtoB(newInstalment, instalmentContainer, context);
                    String description = String.format("%s (%s instalment(s))", newInstalment.getDescription(), newInstalment.getNumberOfInstalments());
                    instalmentContainer.setName(description);
                    instalmentContainer.setRecords(new ArrayList<>());
                }
            }).register();
    }
}
