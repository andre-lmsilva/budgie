package io.geekmind.budgie.model.dto.account;

import io.geekmind.budgie.model.dto.AccountCurrency;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * This DTO holds all the details of an existing {@link io.geekmind.budgie.model.entity.Account} entity.
 *
 * @author Andre Silva
 */
@Data
@EqualsAndHashCode(callSuper = true, exclude = "dependants")
public class ExistingAccount extends BaseAccount {

    private Integer id;

    private Boolean mainAccount;

    private AccountCurrency currency;

    private ExistingAccount parent;

    private List<ExistingAccount> dependants;
}
