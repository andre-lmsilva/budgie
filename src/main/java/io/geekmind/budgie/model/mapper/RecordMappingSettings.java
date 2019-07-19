package io.geekmind.budgie.model.mapper;

import io.geekmind.budgie.model.dto.ExistingRecord;
import io.geekmind.budgie.model.entity.ContainerRecord;
import io.geekmind.budgie.model.entity.Record;
import io.geekmind.budgie.model.entity.RecordContainer;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import net.rakugakibox.spring.boot.orika.OrikaMapperFactoryConfigurer;
import org.springframework.stereotype.Component;

@Component
public class RecordMappingSettings implements OrikaMapperFactoryConfigurer {
    @Override
    public void configure(MapperFactory orikaMapperFactory) {
        orikaMapperFactory.classMap(Record.class, ExistingRecord.class)
            .mapNulls(false)
            .mapNullsInReverse(false)
            .customize(new CustomMapper<Record, ExistingRecord>() {
                @Override
                public void mapAtoB(Record record, ExistingRecord existingRecord, MappingContext context) {
                    super.mapAtoB(record, existingRecord, context);
                    existingRecord.setRecordType(record.getClass().getSimpleName());

                    if (record instanceof ContainerRecord) {
                        RecordContainer container = ((ContainerRecord)record).getRecordContainer();
                        if (null != container) {
                            existingRecord.setContainerId(container.getId());
                        }
                    }
                }
            }).byDefault().register();

    }
}
