package io.geekmind.budgie.model.mapper;

import io.geekmind.budgie.model.dto.AccountCurrency;
import io.geekmind.budgie.model.dto.standard_account.EditStandardAccount;
import io.geekmind.budgie.model.dto.standard_account.ExistingStandardAccount;
import io.geekmind.budgie.model.dto.standard_account.NewStandardAccount;
import io.geekmind.budgie.model.entity.Account;
import io.geekmind.budgie.model.entity.Currency;
import io.geekmind.budgie.model.entity.StandardAccount;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import net.rakugakibox.spring.boot.orika.OrikaMapperFactoryConfigurer;
import org.springframework.stereotype.Component;

@Component
public class StandardAccountMappingSettings implements OrikaMapperFactoryConfigurer {

    private final CurrencyMapper currencyMapper;

    public StandardAccountMappingSettings(CurrencyMapper currencyMapper) {
        this.currencyMapper = currencyMapper;
    }

    @Override
    public void configure(MapperFactory orikaMapperFactory) {
        orikaMapperFactory.classMap(StandardAccount.class, ExistingStandardAccount.class)
            .fieldAToB("id", "id")
            .fieldAToB("mainAccount", "mainAccount")
            .fieldAToB("name", "name")
            .fieldAToB("description", "description")
            .fieldAToB("monthStartingAt", "monthStartingAt")
            .fieldAToB("monthBillingDayAt", "monthBillingDayAt")
            .fieldAToB("showBalanceOnParentAccount", "showBalanceOnParentAccount")
            .fieldAToB("parent", "parent")
            .fieldAToB("archived", "archived")
            .fieldAToB("dependants", "dependants")
            .customize(new CustomMapper<StandardAccount, ExistingStandardAccount>() {
                @Override
                public void mapAtoB(StandardAccount account, ExistingStandardAccount existingAccount, MappingContext context) {
                    super.mapAtoB(account, existingAccount, context);
                    if (null != account.getCurrencyCode()) {
                        existingAccount.setCurrency(
                            currencyMapper.mapFrom(
                                Currency.valueOf(account.getCurrencyCode())
                            )
                        );
                    }
                }
            })
            .register();

        orikaMapperFactory.classMap(NewStandardAccount.class, StandardAccount.class)
            .fieldAToB("name", "name")
            .fieldAToB("parentId", "parent.id")
            .fieldAToB("description", "description")
            .fieldAToB("monthStartingAt", "monthStartingAt")
            .fieldAToB("monthBillingDayAt", "monthBillingDayAt")
            .fieldAToB("showBalanceOnParentAccount", "showBalanceOnParentAccount")
            .fieldAToB("currencyCode", "currencyCode")
            .register();

        orikaMapperFactory.classMap(EditStandardAccount.class, StandardAccount.class)
            .fieldBToA("id", "id")
            .fieldBToA("parent.id", "parentId")
            .field("name", "name")
            .field("description", "description")
            .field("monthStartingAt", "monthStartingAt")
            .field("monthBillingDayAt", "monthBillingDayAt")
            .field("showBalanceOnParentAccount", "showBalanceOnParentAccount")
            .field("currencyCode", "currencyCode")
            .register();
    }
}
