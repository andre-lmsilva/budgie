package io.geekmind.budgie.model.dto.project_account;

import io.geekmind.budgie.model.dto.account.ExistingAccount;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
public class ExistingProjectAccount extends ExistingAccount {

    private BigDecimal targetValue;

}
