package io.geekmind.budgie.model.mapper;

import io.geekmind.budgie.fixture.BalanceFixture;
import io.geekmind.budgie.model.dto.Balance;
import io.geekmind.budgie.model.dto.DependantAccountRecord;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.junit.Before;
import org.junit.Test;

import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

public class DependantAccountRecordMappingTest {

    private MapperFacade mapper;

    @Before
    public void setUp() {
        MapperFactory factory = new DefaultMapperFactory.Builder().build();
        new DependantAccountRecordMappingSettings().configure(factory);
        this.mapper = factory.getMapperFacade();
    }

    @Test
    public void mapping_FromBalanceToDependantAccountRecord_MappingAllFields() {
        Balance fakeBalance = BalanceFixture.getCurrentPeriodBalance();
        DependantAccountRecord result = this.mapper.map(fakeBalance, DependantAccountRecord.class);

        String formattedStartDate = fakeBalance.getBalanceDates().getPeriodStartDate()
            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String formattedEndDate = fakeBalance.getBalanceDates().getPeriodEndDate()
            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String expectedDescription = String.format("Balance of %s account between %s and %s.",
                fakeBalance.getAccount().getName(),
                formattedStartDate,
                formattedEndDate
        );

        assertThat(result)
            .hasFieldOrPropertyWithValue("id", -1)
            .hasFieldOrPropertyWithValue("recordDate", fakeBalance.getBalanceDates().getPeriodBillingDate())
            .hasFieldOrPropertyWithValue("recordValue", fakeBalance.getSummary().getFinalBalance())
            .hasFieldOrPropertyWithValue("account", fakeBalance.getAccount())
            .hasFieldOrPropertyWithValue("description", expectedDescription)
            .hasFieldOrPropertyWithValue("category.id", -1)
            .hasFieldOrPropertyWithValue("category.name", "Account Balance")
            .hasFieldOrPropertyWithValue("category.description", "Dependant account balance result.");
    }

}
