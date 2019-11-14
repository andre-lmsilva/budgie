package io.geekmind.budgie.model.mapper.project_account;

import io.geekmind.budgie.fixture.ProjectAccountFixture;
import io.geekmind.budgie.model.dto.project_account.EditProjectAccount;
import io.geekmind.budgie.model.entity.ProjectAccount;
import io.geekmind.budgie.model.mapper.ProjectAccountMappingSettings;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ProjectAccountToEditProjectAccountMappingTest {

    private ProjectAccount sourceProjectAccount;
    private EditProjectAccount resultEditProjectAccount;

    @Before
    public void setUp() {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        new ProjectAccountMappingSettings(null).configure(mapperFactory);
        this.sourceProjectAccount = ProjectAccountFixture.travelToBahamas();
        this.resultEditProjectAccount = mapperFactory.getMapperFacade().map(this.sourceProjectAccount, EditProjectAccount.class);
    }

    @Test
    public void idAttribute_isMapped() {
        assertThat(this.resultEditProjectAccount)
            .hasFieldOrPropertyWithValue("id", this.sourceProjectAccount.getId());
    }

    @Test
    public void nameAttribute_isMapped() {
        assertThat(this.resultEditProjectAccount)
            .hasFieldOrPropertyWithValue("name", this.sourceProjectAccount.getName());
    }

    @Test
    public void descriptionAttribute_isMapped() {
        assertThat(this.resultEditProjectAccount)
            .hasFieldOrPropertyWithValue("description", this.sourceProjectAccount.getDescription());
    }

    @Test
    public void parentIdAttribute_isMapped() {
        assertThat(this.resultEditProjectAccount)
            .hasFieldOrPropertyWithValue("parentId", this.sourceProjectAccount.getParent().getId());
    }

}
