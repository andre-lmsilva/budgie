package io.geekmind.budgie.model.mapper.project_account;

import io.geekmind.budgie.fixture.ProjectAccountFixture;
import io.geekmind.budgie.model.dto.standard_account.ExistingStandardAccount;
import io.geekmind.budgie.model.entity.ProjectAccount;
import io.geekmind.budgie.model.mapper.ProjectAccountMappingSettings;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class ProjectAccountToExistingStandardAccountMappingTest {

    private ExistingStandardAccount resultStandard;

    @Before
    public void setUp() {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        new ProjectAccountMappingSettings().configure(mapperFactory);
        ProjectAccount sourceProjectAccount = ProjectAccountFixture.travelToBahamas();
        this.resultStandard = mapperFactory.getMapperFacade().map(sourceProjectAccount, ExistingStandardAccount.class);
    }

    @Test
    public void allAttributes_areNotMapped() {
        assertThat(this.resultStandard)
            .hasFieldOrPropertyWithValue("id", null)
            .hasFieldOrPropertyWithValue("name", null)
            .hasFieldOrPropertyWithValue("description", null)
            .hasFieldOrPropertyWithValue("parent", null)
            .hasFieldOrPropertyWithValue("monthStartingAt", null)
            .hasFieldOrPropertyWithValue("monthBillingDayAt", null)
            .hasFieldOrPropertyWithValue("showBalanceOnParentAccount", null)
            .hasFieldOrPropertyWithValue("currency", null)
            .hasFieldOrPropertyWithValue("dependants", null);
    }

}
