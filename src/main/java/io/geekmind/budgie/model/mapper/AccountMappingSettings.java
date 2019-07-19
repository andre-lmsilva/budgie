package io.geekmind.budgie.model.mapper;

import io.geekmind.budgie.model.dto.ExistingAccount;
import io.geekmind.budgie.model.entity.Account;
import ma.glasnost.orika.MapperFactory;
import net.rakugakibox.spring.boot.orika.OrikaMapperFactoryConfigurer;
import org.springframework.stereotype.Component;

@Component
public class AccountMappingSettings implements OrikaMapperFactoryConfigurer {
    @Override
    public void configure(MapperFactory orikaMapperFactory) {
        orikaMapperFactory.classMap(Account.class, ExistingAccount.class)
            .fieldAToB("mainAccount", "mainAccount")
            .mapNulls(false)
            .mapNullsInReverse(false)
            .byDefault()
            .register();
    }
}
