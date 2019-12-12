package io.geekmind.budgie.model.dto.account_parameter;

import io.geekmind.budgie.model.dto.account.ExistingAccount;
import io.geekmind.budgie.model.entity.AccountParameterKey;
import lombok.Data;

import java.io.Serializable;

@Data
public class ExistingAccountParameter implements Serializable {

    private Integer id;

    private ExistingAccount account;

    private AccountParameterKey key;

    private String value;

}
