package io.geekmind.budgie.model.mapper;

import io.geekmind.budgie.fixture.ExistingRecordFixture;
import io.geekmind.budgie.fixture.SingleRecordFixture;
import io.geekmind.budgie.fixture.TransferRecordFixture;
import io.geekmind.budgie.model.dto.ExistingRecord;
import io.geekmind.budgie.model.entity.Record;
import io.geekmind.budgie.model.entity.SingleRecord;
import io.geekmind.budgie.model.entity.TransferRecord;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

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
            .hasFieldOrPropertyWithValue("containerId", null)
            .hasFieldOrPropertyWithValue("upcoming", Boolean.TRUE)
            .hasFieldOrPropertyWithValue("isTaxRefundable", Boolean.TRUE);

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
        fakeExistingRecord.setIsTaxRefundable(false);
        SingleRecord fakeSingleRecord = SingleRecordFixture.getIncomeRecord();
        this.mapper.map(fakeExistingRecord, fakeSingleRecord);

        assertThat(fakeSingleRecord)
            .hasFieldOrPropertyWithValue("id", fakeExistingRecord.getId())
            .hasFieldOrPropertyWithValue("recordDate", fakeExistingRecord.getRecordDate())
            .hasFieldOrPropertyWithValue("category.id", fakeExistingRecord.getCategory().getId())
            .hasFieldOrPropertyWithValue("account.id", fakeExistingRecord.getAccount().getId())
            .hasFieldOrPropertyWithValue("recordValue", fakeExistingRecord.getRecordValue())
            .hasFieldOrPropertyWithValue("isTaxRefundable", fakeExistingRecord.getIsTaxRefundable());

        assertThat(fakeSingleRecord.getDescription()).isNotNull();
    }

    @Test
    public void mapping_fromRecordToExistingRecordWhenRecordIsInThePast_ReturnsUpcomingFalse() {
        Record record = SingleRecordFixture.getIncomeRecord();
        record.setRecordDate(record.getRecordDate().minusDays(2L));

        ExistingRecord result = this.mapper.map(record, ExistingRecord.class);

        assertThat(result).hasFieldOrPropertyWithValue("upcoming", Boolean.FALSE);
    }

    @Test
    public void mapping_fromRecordToExistingRecordWhenRecordIsMoreThan7DaysAhead_ReturnsUpcomingFalse() {
        Record record = SingleRecordFixture.getIncomeRecord();
        record.setRecordDate(record.getRecordDate().plusDays(8L));

        ExistingRecord result = this.mapper.map(record, ExistingRecord.class);

        assertThat(result).hasFieldOrPropertyWithValue("upcoming", Boolean.FALSE);
    }

    @Test
    public void mapping_fromRecordToExistingRecordWhenRecordIs7DaysOrLessAhead_ReturnsUpcomingTrue() {
        Record record = SingleRecordFixture.getIncomeRecord();
        record.setRecordDate(record.getRecordDate().plusDays(7L));

        ExistingRecord result = this.mapper.map(record, ExistingRecord.class);

        assertThat(result).hasFieldOrPropertyWithValue("upcoming", Boolean.TRUE);
    }
}
