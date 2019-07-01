package io.geekmind.budgie.model.mapper;

import io.geekmind.budgie.model.dto.NewInstalment;
import io.geekmind.budgie.model.entity.InstalmentRecord;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import net.rakugakibox.spring.boot.orika.OrikaMapperFactoryConfigurer;
import org.springframework.stereotype.Component;

@Component
public class InstalmentRecordMappingSettings implements OrikaMapperFactoryConfigurer {
    @Override
    public void configure(MapperFactory orikaMapperFactory) {
        orikaMapperFactory.classMap(NewInstalment.class, InstalmentRecord.class)
            .byDefault()
            .field("accountId", "account.id")
            .field("categoryId", "category.id")
            .field("instalmentValue", "recordValue")
            .customize(new CustomMapper<NewInstalment, InstalmentRecord>() {
                @Override
                public void mapAtoB(NewInstalment newInstalment, InstalmentRecord instalmentRecord, MappingContext context) {
                    super.mapAtoB(newInstalment, instalmentRecord, context);
                    instalmentRecord.setDescription(
                        String.format("%s (%%s of %s)", newInstalment.getDescription(), newInstalment.getNumberOfInstalments())
                    );
                }
            }).register();

    }
}
