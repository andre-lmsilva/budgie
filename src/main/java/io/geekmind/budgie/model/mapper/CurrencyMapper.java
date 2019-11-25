package io.geekmind.budgie.model.mapper;

import io.geekmind.budgie.model.dto.account.AccountCurrency;
import io.geekmind.budgie.model.entity.Currency;

@FunctionalInterface
public interface CurrencyMapper {
    AccountCurrency mapFrom(Currency currency);
}
