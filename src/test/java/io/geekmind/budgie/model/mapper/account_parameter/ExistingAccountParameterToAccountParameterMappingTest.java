package io.geekmind.budgie.model.mapper.account_parameter;

import io.geekmind.budgie.fixture.ExistingAccountParameterFixture;
import io.geekmind.budgie.model.entity.AccountParameter;
import io.geekmind.budgie.model.mapper.AccountParameterMappingSettings;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ExistingAccountParameterToAccountParameterMappingTest {

    private AccountParameter resultAccountParameter;

    @Before
    public void setUp() {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        new AccountParameterMappingSettings().configure(mapperFactory);
        this.resultAccountParameter = mapperFactory.getMapperFacade().map(
            ExistingAccountParameterFixture.lastLoginTimestamp(),
            AccountParameter.class
        );
    }

    @Test
    public void allAttributes_areNotMapped() {
        assertThat(this.resultAccountParameter)
            .hasNoNullFieldsOrPropertiesExcept("id", "account", "key", "value");
    }

}
