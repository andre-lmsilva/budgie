package io.geekmind.budgie.model.mapper;

import io.geekmind.budgie.model.dto.NewImportedRecord;
import io.geekmind.budgie.model.entity.ImportedRecord;
import ma.glasnost.orika.MapperFactory;
import net.rakugakibox.spring.boot.orika.OrikaMapperFactoryConfigurer;
import org.springframework.stereotype.Component;

@Component
public class ImportedRecordMappingSettings implements OrikaMapperFactoryConfigurer {
    @Override
    public void configure(MapperFactory orikaMapperFactory) {
        orikaMapperFactory.classMap(NewImportedRecord.class, ImportedRecord.class)
            .field("importedFileId", "importedFile.id")
            .field("categoryId" ,"category.id")
            .field("accountId", "account.id")
            .field("md5RecordHash", "sourceMd5Hash")
            .byDefault()
            .register();
    }
}
