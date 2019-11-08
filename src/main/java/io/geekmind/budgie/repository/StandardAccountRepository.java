package io.geekmind.budgie.repository;

import io.geekmind.budgie.model.entity.Account;
import io.geekmind.budgie.model.entity.StandardAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StandardAccountRepository extends JpaRepository<StandardAccount, Integer> {

    List<Account> findByArchivedFalse();

    @Query("FROM Account account WHERE account.mainAccount = true")
    Optional<Account> findMainAccount();

    @Query("FROM Account account WHERE account.showBalanceOnParentAccount = false")
    List<Account> findNonDependantAccounts();

}
