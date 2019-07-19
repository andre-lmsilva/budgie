package io.geekmind.budgie.model.mapper;

import io.geekmind.budgie.fixture.NewInstalmentFixture;
import io.geekmind.budgie.model.dto.NewInstalment;
import io.geekmind.budgie.model.entity.InstalmentRecord;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class InstalmentRecordMappingTest {

    private MapperFacade mapper;

    @Before
    public void setUp() {
        MapperFactory factory = new DefaultMapperFactory.Builder().build();
        new InstalmentRecordMappingSettings().configure(factory);
        this.mapper = factory.getMapperFacade();
    }

    @Test
    public void mapping_FromNewInstalmentToInstalmentRecord_MapsFields() {
        NewInstalment fakeNewInstalment = NewInstalmentFixture.get();
        InstalmentRecord result = this.mapper.map(fakeNewInstalment, InstalmentRecord.class);
        String expectedDescription = String.format(
            "%s (%%s of %s)",
            fakeNewInstalment.getDescription(),
            fakeNewInstalment.getNumberOfInstalments()
        );


        assertThat(result)
            .hasFieldOrPropertyWithValue("id", null)
            .hasFieldOrPropertyWithValue("recordDate", null)
            .hasFieldOrPropertyWithValue("account.id", fakeNewInstalment.getAccountId())
            .hasFieldOrPropertyWithValue("category.id", fakeNewInstalment.getCategoryId())
            .hasFieldOrPropertyWithValue("description", expectedDescription)
            .hasFieldOrPropertyWithValue("recordValue", fakeNewInstalment.getInstalmentValue());

    }

}