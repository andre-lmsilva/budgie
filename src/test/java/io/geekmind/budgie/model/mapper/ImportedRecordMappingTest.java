package io.geekmind.budgie.model.mapper;

import io.geekmind.budgie.fixture.NewImportedRecordFixture;
import io.geekmind.budgie.model.dto.NewImportedRecord;
import io.geekmind.budgie.model.entity.ImportedRecord;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ImportedRecordMappingTest {

    private MapperFacade mapper;

    @Before
    public void setUp() {
        MapperFactory factory = new DefaultMapperFactory.Builder().build();
        new ImportedRecordMappingSettings().configure(factory);
        this.mapper = factory.getMapperFacade();
    }

    @Test
    public void mapping_FromNewImportedRecordToImportedRecord_MapsAllFields() {
        NewImportedRecord fakeNewImportedRecord = NewImportedRecordFixture.get();
        ImportedRecord result = this.mapper.map(fakeNewImportedRecord, ImportedRecord.class);

        assertThat(result)
            .hasFieldOrPropertyWithValue("id", null)
            .hasFieldOrPropertyWithValue("account.id", fakeNewImportedRecord.getAccountId())
            .hasFieldOrPropertyWithValue("category.id", fakeNewImportedRecord.getCategoryId())
            .hasFieldOrPropertyWithValue("importedFile.id", fakeNewImportedRecord.getImportedFileId())
            .hasFieldOrPropertyWithValue("recordDate", fakeNewImportedRecord.getRecordDate())
            .hasFieldOrPropertyWithValue("description", fakeNewImportedRecord.getDescription())
            .hasFieldOrPropertyWithValue("recordValue", fakeNewImportedRecord.getRecordValue())
            .hasFieldOrPropertyWithValue("sourceMd5Hash", fakeNewImportedRecord.getMd5RecordHash());
    }

}
