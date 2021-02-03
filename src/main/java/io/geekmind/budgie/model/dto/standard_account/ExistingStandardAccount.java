package io.geekmind.budgie.model.dto.standard_account;

import io.geekmind.budgie.model.dto.account.ExistingAccount;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<ExistingStandardAccount> getActiveDependantAccounts() {
        if (null == dependants) {
            return Collections.emptyList();
        }
        return this.dependants
            .stream()
            .filter(dependant -> !dependant.getArchived())
            .filter(dependant -> dependant instanceof ExistingStandardAccount)
            .map(dependant -> (ExistingStandardAccount)dependant)
            .collect(Collectors.toList());
    }

}
