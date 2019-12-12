package io.geekmind.budgie.model.dto.account;

import io.geekmind.budgie.model.dto.account_parameter.ExistingAccountParameter;
import lombok.Data;

import java.util.List;

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

    private List<ExistingAccountParameter> parameters;
}
