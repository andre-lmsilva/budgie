package io.geekmind.budgie.model.dto.account;

import io.geekmind.budgie.model.dto.AccountCurrency;
import io.geekmind.budgie.model.entity.Account;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * This DTO holds all the details of an existing {@link io.geekmind.budgie.model.entity.Account} entity.
 *
 * @author Andre Silva
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ExistingAccount extends BaseAccount {

    private Integer id;

    private Boolean mainAccount;

    private AccountCurrency currency;

    private Account parent;

    private List<Account> dependants;
}
