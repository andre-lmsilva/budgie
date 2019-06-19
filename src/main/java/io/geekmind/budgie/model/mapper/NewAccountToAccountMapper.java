package io.geekmind.budgie.model.mapper;

import io.geekmind.budgie.model.dto.NewAccount;
import io.geekmind.budgie.model.entity.Account;
import org.springframework.stereotype.Component;

@Component(NewAccountToAccountMapper.QUALIFIER)
public class NewAccountToAccountMapper implements Mapper<NewAccount, Account> {

    public static final String QUALIFIER = "newAccountToAccountMapper";

    @Override
    public Account mapTo(NewAccount source) {
        Account account = new Account();
        account.setName(source.getName());
        account.setDescription(source.getDescription());
        account.setMonthStartingAt(source.getMonthStartingAt());
        account.setMonthBillingDayAt(source.getMonthBillingDayAt());
        account.setShowBalanceOnMainAccount(source.getShowBalanceOnMainAccount());
        return account;
    }

    @Override
    public NewAccount mapFrom(Account target) {
        throw new UnsupportedOperationException("There is not mapping operation in that direction.");
    }
}
