package io.geekmind.budgie.model.mapper;

import io.geekmind.budgie.model.dto.AccountCurrency;
import io.geekmind.budgie.model.entity.Currency;
import org.springframework.stereotype.Component;

@Component
public class DefaultCurrencyMapper implements CurrencyMapper {

    @Override
    public AccountCurrency mapFrom(Currency currency) {
        return new AccountCurrency(
            currency.currencyCode(), currency.currencyName(), currency.currencySymbol()
        );
    }

}
