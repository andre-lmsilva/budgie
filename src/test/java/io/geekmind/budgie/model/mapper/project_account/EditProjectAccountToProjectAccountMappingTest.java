package io.geekmind.budgie.model.mapper.project_account;

import io.geekmind.budgie.fixture.EditProjectAccountFixture;
import io.geekmind.budgie.model.dto.project_account.EditProjectAccount;
import io.geekmind.budgie.model.entity.ProjectAccount;
import io.geekmind.budgie.model.mapper.ProjectAccountMappingSettings;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EditProjectAccountToProjectAccountMappingTest {

    private EditProjectAccount sourceEditProjectAccount;
    private ProjectAccount resultProjectAccount;

    @Before
    public void setUp() {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        new ProjectAccountMappingSettings().configure(mapperFactory);
        this.sourceEditProjectAccount = EditProjectAccountFixture.macBookPro();
        this.resultProjectAccount = mapperFactory.getMapperFacade().map(this.sourceEditProjectAccount, ProjectAccount.class);
    }

    @Test
    public void idAttribute_isNotMapped() {
        assertThat(this.resultProjectAccount)
            .hasFieldOrPropertyWithValue("id", null);
    }

    @Test
    public void nameAttribute_isMapped() {
        assertThat(this.resultProjectAccount)
            .hasFieldOrPropertyWithValue("name", this.sourceEditProjectAccount.getName());
    }

    @Test
    public void descriptionAttribute_isMapped() {
        assertThat(this.resultProjectAccount)
            .hasFieldOrPropertyWithValue("description", this.sourceEditProjectAccount.getDescription());
    }

    @Test
    public void parentIdAttribute_isMapped() {
        assertThat(this.resultProjectAccount)
            .hasFieldOrPropertyWithValue("parent.id", this.sourceEditProjectAccount.getParentId());
    }

    @Test
    public void targetValueAttribute_isMapped() {
        assertThat(this.resultProjectAccount)
            .hasFieldOrPropertyWithValue("targetValue", this.sourceEditProjectAccount.getTargetValue());
    }

}
