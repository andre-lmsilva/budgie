package io.geekmind.budgie.model.mapper;

import io.geekmind.budgie.model.dto.account.AccountCurrency;
import io.geekmind.budgie.model.entity.Currency;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DefaultCurrencyMapperTest {

    private Currency sourceCurrency;
    private AccountCurrency resultAccountCurrency;

    @Before
    public void setUp() {
       this.sourceCurrency = Currency.EUR;
       this.resultAccountCurrency = new DefaultCurrencyMapper().mapFrom(this.sourceCurrency);
    }

    @Test
    public void currencyCodeAttribute_isMapped() {
        assertThat(resultAccountCurrency)
            .hasFieldOrPropertyWithValue("currencyCode", this.sourceCurrency.currencyCode());
    }

    @Test
    public void currencyNameAttribute_isMapped() {
        assertThat(resultAccountCurrency)
            .hasFieldOrPropertyWithValue("currencyName", this.sourceCurrency.currencyName());
    }

    @Test
    public void currencySymbolAttribute_isMapped() {
        assertThat(resultAccountCurrency)
            .hasFieldOrPropertyWithValue("currencySymbol", this.sourceCurrency.currencySymbol());
    }

}