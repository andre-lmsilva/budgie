package io.geekmind.budgie.model.dto.standard_account;

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
public class ExistingStandardAccount extends BaseStandardAccount {

    private Integer id;

    private Boolean mainAccount;

    private AccountCurrency currency;

    private ExistingStandardAccount parent;

    private List<ExistingStandardAccount> dependants;
}
