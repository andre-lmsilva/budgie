package io.geekmind.budgie.model.mapper;

import io.geekmind.budgie.model.dto.ExistingRecord;
import io.geekmind.budgie.model.entity.*;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import net.rakugakibox.spring.boot.orika.OrikaMapperFactoryConfigurer;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class RecordMappingSettings implements OrikaMapperFactoryConfigurer {
    @Override
    public void configure(MapperFactory orikaMapperFactory) {

        LocalDate today = LocalDate.now();
        LocalDate oneWeekTime = today.plusWeeks(1L);

        orikaMapperFactory.classMap(Record.class, ExistingRecord.class)
            .mapNulls(false)
            .mapNullsInReverse(false)
            .customize(new CustomMapper<Record, ExistingRecord>() {
                @Override
                public void mapAtoB(Record record, ExistingRecord existingRecord, MappingContext context) {
                    super.mapAtoB(record, existingRecord, context);
                    existingRecord.setRecordType(record.getClass().getSimpleName());

                    if (record instanceof ContainerRecord) {
                        RecordContainer container = ((ContainerRecord) record).getRecordContainer();
                        if (null != container) {
                            existingRecord.setContainerId(container.getId());
                        }
                    }

                    if (record.getRecordDate().compareTo(today) >= 0 &&
                        record.getRecordDate().compareTo(oneWeekTime) <= 0) {
                        existingRecord.setUpcoming(Boolean.TRUE);
                    }

                    if (null != record.getAccount().getParameters()) {
                        existingRecord.setLastCreated(
                                record.getAccount().getParameters().stream()
                                        .filter(accountParameter -> accountParameter.getKey().equals(AccountParameterKey.LAST_CREATED_RECORD.name()))
                                        .findFirst()
                                        .map(accountParameter -> accountParameter.getValue().equals(record.getId().toString()))
                                        .orElse(Boolean.FALSE)
                        );
                    }

                }
            }).byDefault().register();
    }
}
