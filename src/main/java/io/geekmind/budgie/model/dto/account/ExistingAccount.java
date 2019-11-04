package io.geekmind.budgie.model.dto.account;

import io.geekmind.budgie.model.dto.AccountCurrency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * This DTO holds all the details of an existing {@link io.geekmind.budgie.model.entity.Account} entity.
 *
 * @author Andre Silva
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class ExistingAccount extends BaseAccount {

    private Integer id;

    private Boolean mainAccount;

    private AccountCurrency currency;
}
