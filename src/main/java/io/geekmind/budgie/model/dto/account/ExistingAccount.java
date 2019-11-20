package io.geekmind.budgie.model.dto.account;

import io.geekmind.budgie.model.dto.AccountCurrency;
import lombok.Data;

@Data
public class ExistingAccount {

    private Integer id;

    private String name;

    private String description;

    private Integer monthStartingAt;

    private Integer monthBillingDayAt;

    private ExistingAccount parent;

    private Boolean archived;

    private AccountCurrency currency;

}
