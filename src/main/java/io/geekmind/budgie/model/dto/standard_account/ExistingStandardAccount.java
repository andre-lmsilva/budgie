package io.geekmind.budgie.model.dto.standard_account;

import io.geekmind.budgie.model.dto.account.ExistingAccount;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * This DTO holds all the details of an existing {@link io.geekmind.budgie.model.entity.Account} entity.
 *
 * @author Andre Silva
 */
@Data
@EqualsAndHashCode(callSuper = true, exclude = {"dependants"})
public class ExistingStandardAccount extends ExistingAccount {

    private Boolean mainAccount;

    private Boolean showBalanceOnParentAccount;

    private List<ExistingAccount> dependants;

}
