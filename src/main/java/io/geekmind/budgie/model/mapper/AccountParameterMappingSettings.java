package io.geekmind.budgie.model.mapper;

import io.geekmind.budgie.model.dto.account_parameter.ExistingAccountParameter;
import io.geekmind.budgie.model.entity.AccountParameter;
import io.geekmind.budgie.model.entity.AccountParameterKey;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import net.rakugakibox.spring.boot.orika.OrikaMapperFactoryConfigurer;
import org.springframework.stereotype.Component;

@Component
public class AccountParameterMappingSettings implements OrikaMapperFactoryConfigurer {
    @Override
    public void configure(MapperFactory orikaMapperFactory) {
        orikaMapperFactory.classMap(AccountParameter.class, ExistingAccountParameter.class)
            .fieldAToB("id", "id")
            .fieldAToB("account", "account")
            .fieldAToB("value", "value")
            .customize(new CustomMapper<AccountParameter, ExistingAccountParameter>() {
                @Override
                public void mapAtoB(AccountParameter accountParameter, ExistingAccountParameter existingAccountParameter, MappingContext context) {
                    super.mapAtoB(accountParameter, existingAccountParameter, context);
                    for(AccountParameterKey key: AccountParameterKey.values()) {
                        if (key.name().equals(accountParameter.getKey())) {
                            existingAccountParameter.setKey(key);
                            return;
                        }
                    }
                }
            }).register();

    }
}
