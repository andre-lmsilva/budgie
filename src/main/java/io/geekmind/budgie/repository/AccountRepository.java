package io.geekmind.budgie.repository;

import io.geekmind.budgie.model.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {

    @Query("FROM Account account WHERE account.mainAccount = true")
    Optional<Account> findMainAccount();

    @Query("FROM Account account WHERE account.showBalanceOnMainAccount = true")
    List<Account> findDependantAccounts();

    @Query("FROM Account account WHERE account.showBalanceOnMainAccount = false")
    List<Account> findNonDependantAccounts();

}
