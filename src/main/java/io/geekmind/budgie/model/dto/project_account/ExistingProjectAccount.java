package io.geekmind.budgie.model.dto.project_account;

import io.geekmind.budgie.model.dto.AccountCurrency;
import io.geekmind.budgie.model.dto.standard_account.ExistingStandardAccount;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ExistingProjectAccount extends BaseProjectAccount {

    private Integer id;

    private Integer monthStartingAt;

    private Integer monthBillingDayAt;

    private ExistingStandardAccount parent;

    private AccountCurrency currency;

}
