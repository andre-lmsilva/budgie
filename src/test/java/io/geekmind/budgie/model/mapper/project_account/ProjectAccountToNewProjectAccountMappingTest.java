package io.geekmind.budgie.model.mapper.project_account;

import io.geekmind.budgie.fixture.ProjectAccountFixture;
import io.geekmind.budgie.model.dto.project_account.NewProjectAccount;
import io.geekmind.budgie.model.entity.ProjectAccount;
import io.geekmind.budgie.model.mapper.ProjectAccountMappingSettings;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ProjectAccountToNewProjectAccountMappingTest {

    private NewProjectAccount resultNewProjectAccount;

    @Before
    public void setUp() {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        new ProjectAccountMappingSettings().configure(mapperFactory);
        ProjectAccount sourceProjectAccount = ProjectAccountFixture.travelToBahamas();
        this.resultNewProjectAccount = mapperFactory.getMapperFacade().map(sourceProjectAccount, NewProjectAccount.class);
    }

    @Test
    public void allAttributes_areNotMapped() {
        assertThat(this.resultNewProjectAccount)
            .hasFieldOrPropertyWithValue("name", null)
            .hasFieldOrPropertyWithValue("description", null)
            .hasFieldOrPropertyWithValue("parentId", null)
            .hasFieldOrPropertyWithValue("targetValue", null);
    }

}
