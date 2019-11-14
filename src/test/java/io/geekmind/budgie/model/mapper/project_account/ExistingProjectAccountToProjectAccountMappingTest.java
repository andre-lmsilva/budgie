package io.geekmind.budgie.model.mapper.project_account;

import io.geekmind.budgie.fixture.ExistingProjectAccountFixture;
import io.geekmind.budgie.model.dto.project_account.ExistingProjectAccount;
import io.geekmind.budgie.model.entity.ProjectAccount;
import io.geekmind.budgie.model.mapper.ProjectAccountMappingSettings;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ExistingProjectAccountToProjectAccountMappingTest {

    private ExistingProjectAccount sourceExistingProjectAccount;
    private ProjectAccount resultProjectAccount;

    @Before
    public void setUp() {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        new ProjectAccountMappingSettings(null).configure(mapperFactory);
        this.sourceExistingProjectAccount = ExistingProjectAccountFixture.travelToBahamas();
        this.resultProjectAccount = mapperFactory.getMapperFacade().map(this.sourceExistingProjectAccount, ProjectAccount.class);
    }

    @Test
    public void allAttributes_areNotMapped() {
        assertThat(this.resultProjectAccount)
            .hasFieldOrPropertyWithValue("id", null)
            .hasFieldOrPropertyWithValue("name", null)
            .hasFieldOrPropertyWithValue("description", null)
            .hasFieldOrPropertyWithValue("monthStartingAt", null)
            .hasFieldOrPropertyWithValue("monthBillingDayAt", null)
            .hasFieldOrPropertyWithValue("currencyCode", null)
            .hasFieldOrPropertyWithValue("parent", null)
            .hasFieldOrPropertyWithValue("targetValue", null);
    }
}
