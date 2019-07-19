package io.geekmind.budgie.model.mapper;

import io.geekmind.budgie.fixture.NewSingleRecordFixture;
import io.geekmind.budgie.model.dto.NewSingleRecord;
import io.geekmind.budgie.model.entity.SingleRecord;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SingleRecordMappingTest {

    private MapperFacade mapper;

    @Before
    public void setUp() {
        MapperFactory factory = new DefaultMapperFactory.Builder().build();
        new SingleRecordMappingSettings().configure(factory);
        this.mapper = factory.getMapperFacade();
    }

    @Test
    public void mapping_FromNewSingleRecordToSingleRecord_MapsAllFields() {
        NewSingleRecord fakeNewSingleRecord = NewSingleRecordFixture.get();
        SingleRecord result = this.mapper.map(fakeNewSingleRecord, SingleRecord.class);

        assertThat(result)
            .hasFieldOrPropertyWithValue("id", null)
            .hasFieldOrPropertyWithValue("recordDate", fakeNewSingleRecord.getRecordDate())
            .hasFieldOrPropertyWithValue("description", fakeNewSingleRecord.getDescription())
            .hasFieldOrPropertyWithValue("recordValue", fakeNewSingleRecord.getRecordValue())
            .hasFieldOrPropertyWithValue("category.id", fakeNewSingleRecord.getCategoryId())
            .hasFieldOrPropertyWithValue("account.id", fakeNewSingleRecord.getAccountId());
    }

}
