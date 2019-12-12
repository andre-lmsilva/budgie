package io.geekmind.budgie.model.mapper.account_parameter;

import io.geekmind.budgie.fixture.AccountParameterFixture;
import io.geekmind.budgie.model.dto.account_parameter.ExistingAccountParameter;
import io.geekmind.budgie.model.entity.AccountParameter;
import io.geekmind.budgie.model.entity.AccountParameterKey;
import io.geekmind.budgie.model.mapper.AccountParameterMappingSettings;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountParameterToExistingAccountParameterMappingTest {

    private AccountParameter sourceAccountParameter;
    private ExistingAccountParameter resultExistingAccountParameter;

    @Before
    public void setUp() {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        new AccountParameterMappingSettings().configure(mapperFactory);
        this.sourceAccountParameter = AccountParameterFixture.currentLoginTimestamp();
        this.resultExistingAccountParameter = mapperFactory.getMapperFacade().map(
            this.sourceAccountParameter,
            ExistingAccountParameter.class
        );
    }

    @Test
    public void idAttribute_isMapped() {
        assertThat(this.resultExistingAccountParameter)
            .hasFieldOrPropertyWithValue("id", this.resultExistingAccountParameter.getId());
    }

    @Test
    public void accountAttribute_isMapped() {
        assertThat(this.resultExistingAccountParameter)
            .hasFieldOrPropertyWithValue("account.id", this.sourceAccountParameter.getAccount().getId());
    }

    @Test
    public void keyAttribute_isMapped() {
        assertThat(this.resultExistingAccountParameter)
            .hasFieldOrPropertyWithValue("key", AccountParameterKey.CURRENT_LOGIN_TIMESTAMP);
    }

    @Test
    public void valueAttribute_isMapped() {
        assertThat(this.resultExistingAccountParameter)
            .hasFieldOrPropertyWithValue("value", this.sourceAccountParameter.getValue());
    }



}
