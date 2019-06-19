package io.geekmind.budgie.model.mapper;

import io.geekmind.budgie.model.dto.ExistingAccount;
import io.geekmind.budgie.model.entity.Account;
import org.springframework.stereotype.Component;

@Component(AccountToExistingAccountMapper.QUALIFIER)
public class AccountToExistingAccountMapper implements Mapper<Account, ExistingAccount> {

    public static final String QUALIFIER = "accountToExistingAccountMapper";

    @Override
    public ExistingAccount mapTo(Account source) {
        ExistingAccount existingAccount = new ExistingAccount();
        existingAccount.setId(source.getId());
        existingAccount.setName(source.getName());
        existingAccount.setDescription(source.getDescription());
        existingAccount.setMonthStartingAt(source.getMonthStartingAt());
        existingAccount.setMonthBillingDayAt(source.getMonthBillingDayAt());
        existingAccount.setMainAccount(source.getMainAccount());
        existingAccount.setShowBalanceOnMainAccount(source.getShowBalanceOnMainAccount());
        return existingAccount;
    }

    @Override
    public Account mapFrom(ExistingAccount target) {
        throw new UnsupportedOperationException("There is not mapping operation in that direction.");
    }
}
