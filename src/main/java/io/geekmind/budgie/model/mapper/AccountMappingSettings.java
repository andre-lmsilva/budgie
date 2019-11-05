package io.geekmind.budgie.model.mapper;

import io.geekmind.budgie.model.dto.AccountCurrency;
import io.geekmind.budgie.model.dto.account.EditAccount;
import io.geekmind.budgie.model.dto.account.ExistingAccount;
import io.geekmind.budgie.model.dto.account.NewAccount;
import io.geekmind.budgie.model.entity.Account;
import io.geekmind.budgie.model.entity.Currency;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import net.rakugakibox.spring.boot.orika.OrikaMapperFactoryConfigurer;
import org.springframework.stereotype.Component;

@Component
public class AccountMappingSettings implements OrikaMapperFactoryConfigurer {

    private final CurrencyMapper currencyMapper;

    public AccountMappingSettings(CurrencyMapper currencyMapper) {
        this.currencyMapper = currencyMapper;
    }

    @Override
    public void configure(MapperFactory orikaMapperFactory) {
        orikaMapperFactory.classMap(Account.class, ExistingAccount.class)
            .fieldAToB("id", "id")
            .fieldAToB("mainAccount", "mainAccount")
            .fieldAToB("name", "name")
            .fieldAToB("description", "description")
            .fieldAToB("monthStartingAt", "monthStartingAt")
            .fieldAToB("monthBillingDayAt", "monthBillingDayAt")
            .fieldAToB("showBalanceOnMainAccount", "showBalanceOnMainAccount")
            .customize(new CustomMapper<Account, ExistingAccount>() {
                @Override
                public void mapAtoB(Account account, ExistingAccount existingAccount, MappingContext context) {
                    super.mapAtoB(account, existingAccount, context);
                    if (null != account.getCurrencyCode()) {
                        AccountCurrency currency = currencyMapper.mapFrom(
                                Currency.valueOf(account.getCurrencyCode())
                        );
                        existingAccount.setCurrency(currency);
                    }
                }
            })
            .register();

        orikaMapperFactory.classMap(NewAccount.class, Account.class)
            .fieldAToB("name", "name")
            .fieldAToB("description", "description")
            .fieldAToB("monthStartingAt", "monthStartingAt")
            .fieldAToB("monthBillingDayAt", "monthBillingDayAt")
            .fieldAToB("showBalanceOnMainAccount", "showBalanceOnMainAccount")
            .fieldAToB("currencyCode", "currencyCode")
            .register();

        orikaMapperFactory.classMap(EditAccount.class, Account.class)
            .fieldBToA("id", "id")
            .byDefault()
            .register();
    }
}
