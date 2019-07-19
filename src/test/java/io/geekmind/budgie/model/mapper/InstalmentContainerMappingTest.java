package io.geekmind.budgie.model.mapper;

import io.geekmind.budgie.fixture.NewInstalmentFixture;
import io.geekmind.budgie.model.dto.NewInstalment;
import io.geekmind.budgie.model.entity.InstalmentContainer;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class InstalmentContainerMappingTest {

    private MapperFacade mapper;

    @Before
    public void setUp() {
        MapperFactory factory = new DefaultMapperFactory.Builder().build();
        new InstalmentContainerMappingSettings().configure(factory);
        this.mapper = factory.getMapperFacade();
    }

    @Test
    public void mappings_FromNewInstallmentToInstalmentContainer_MapsFields() {
        NewInstalment fakeNewInstalment = NewInstalmentFixture.get();
        String expectedName = String.format("%s (%s instalment(s))",
                fakeNewInstalment.getDescription(),
                fakeNewInstalment.getNumberOfInstalments()
        );

        InstalmentContainer result = this.mapper.map(fakeNewInstalment, InstalmentContainer.class);

        assertThat(result)
            .hasFieldOrPropertyWithValue("name", expectedName);
        assertThat(result.getRecords()).isNotNull().isEmpty();
    }

}
