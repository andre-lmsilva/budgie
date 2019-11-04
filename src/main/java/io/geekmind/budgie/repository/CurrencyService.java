package io.geekmind.budgie.repository;

import io.geekmind.budgie.model.dto.AccountCurrency;
import io.geekmind.budgie.model.entity.Currency;
import io.geekmind.budgie.model.mapper.CurrencyMapper;
import io.geekmind.budgie.model.mapper.DefaultCurrencyMapper;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides a high level and abstract access to currency information available on the application.
 *
 * @author Andre Silva
 */
@Service
public class CurrencyService {

    private final CurrencyMapper currencyMapper;
    private List<AccountCurrency> currencies;

    public CurrencyService(CurrencyMapper currencyMapper) {
        this.currencyMapper = currencyMapper;
    }

    @PostConstruct
    public void initialize() {
        this.loadAll();
    }

    /**
     * Returns all available currencies.
     * @return A list of {@link AccountCurrency} containing all the details about available currencies on the application.
     */
    public List<AccountCurrency> loadAll() {
        if (null == this.currencies) {
            currencies = Arrays.stream(Currency.values())
                .map(this.currencyMapper::mapFrom)
                .collect(Collectors.toList());
        }
        return this.currencies;
    }
}
