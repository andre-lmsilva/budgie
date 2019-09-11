package io.geekmind.budgie.model.mapper;

import io.geekmind.budgie.fixture.ExistingRecordFixture;
import io.geekmind.budgie.fixture.SingleRecordFixture;
import io.geekmind.budgie.fixture.TransferRecordFixture;
import io.geekmind.budgie.model.dto.ExistingRecord;
import io.geekmind.budgie.model.entity.SingleRecord;
import io.geekmind.budgie.model.entity.TransferRecord;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class RecordMappingTest {

    private MapperFacade mapper;

    @Before
    public void setUp() {
        MapperFactory factory = new DefaultMapperFactory.Builder().build();
        new RecordMappingSettings().configure(factory);
        this.mapper = factory.getMapperFacade();
    }

    @Test
    public void mapping_FromSingleRecordToExistingRecord_MapsAllFields() {
        SingleRecord fakeSingleRecord = SingleRecordFixture.getIncomeRecord();
        ExistingRecord result = this.mapper.map(fakeSingleRecord, ExistingRecord.class);
        assertThat(result)
            .hasFieldOrPropertyWithValue("id", fakeSingleRecord.getId())
            .hasFieldOrPropertyWithValue("recordDate", fakeSingleRecord.getRecordDate())
            .hasFieldOrPropertyWithValue("description", fakeSingleRecord.getDescription())
            .hasFieldOrPropertyWithValue("category.id", fakeSingleRecord.getCategory().getId())
            .hasFieldOrPropertyWithValue("account.id", fakeSingleRecord.getAccount().getId())
            .hasFieldOrPropertyWithValue("recordValue", fakeSingleRecord.getRecordValue())
            .hasFieldOrPropertyWithValue("recordType", "SingleRecord")
            .hasFieldOrPropertyWithValue("containerId", null);
    }

    @Test
    public void mapping_FromTransferRecordToExistingRecord_MapsContainerIdAsWell() {
        TransferRecord fakeTransferRecord = TransferRecordFixture.get();
        ExistingRecord result = this.mapper.map(fakeTransferRecord, ExistingRecord.class);
        assertThat(result)
            .hasFieldOrPropertyWithValue("containerId", fakeTransferRecord.getRecordContainer().getId())
            .hasFieldOrPropertyWithValue("recordType", "TransferRecord");
    }

    @Test
    public void merging_FromExistingRecordToAlreadyFilledSingleRecord_DoesNotMapNull() {
        ExistingRecord fakeExistingRecord = ExistingRecordFixture.getWithValue(BigDecimal.valueOf(-500D));
        fakeExistingRecord.setDescription(null);
        SingleRecord fakeSingleRecord = SingleRecordFixture.getIncomeRecord();
        this.mapper.map(fakeExistingRecord, fakeSingleRecord);

        assertThat(fakeSingleRecord)
            .hasFieldOrPropertyWithValue("id", fakeExistingRecord.getId())
            .hasFieldOrPropertyWithValue("recordDate", fakeExistingRecord.getRecordDate())
            .hasFieldOrPropertyWithValue("category.id", fakeExistingRecord.getCategory().getId())
            .hasFieldOrPropertyWithValue("account.id", fakeExistingRecord.getAccount().getId())
            .hasFieldOrPropertyWithValue("recordValue", fakeExistingRecord.getRecordValue());

        assertThat(fakeSingleRecord.getDescription()).isNotNull();
    }
}
