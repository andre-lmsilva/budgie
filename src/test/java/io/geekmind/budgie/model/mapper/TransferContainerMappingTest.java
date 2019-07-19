package io.geekmind.budgie.model.mapper;

import io.geekmind.budgie.fixture.NewTransferFixture;
import io.geekmind.budgie.model.dto.NewTransfer;
import io.geekmind.budgie.model.entity.TransferContainer;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TransferContainerMappingTest {

    private MapperFacade mapper;

    @Before
    public void setUp() {
        MapperFactory factory = new DefaultMapperFactory.Builder().build();
        new TransferContainerMappingSettings().configure(factory);
        this.mapper = factory.getMapperFacade();
    }

    @Test
    public void mapping_FromNewTransferToTransferContainer_MapsAttributes() {
        NewTransfer fakeNewTransfer = NewTransferFixture.get();
        TransferContainer result = this.mapper.map(fakeNewTransfer, TransferContainer.class);
        assertThat(result)
            .hasFieldOrPropertyWithValue("id", null)
            .hasFieldOrPropertyWithValue("name", fakeNewTransfer.getDescription())
            .hasFieldOrPropertyWithValue("records", null);
    }

}
