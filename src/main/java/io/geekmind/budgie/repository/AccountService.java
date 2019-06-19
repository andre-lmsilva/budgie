package io.geekmind.budgie.repository;

import io.geekmind.budgie.model.dto.ExistingAccount;
import io.geekmind.budgie.model.dto.NewAccount;
import io.geekmind.budgie.model.entity.Account;
import io.geekmind.budgie.model.mapper.AccountToExistingAccountMapper;
import io.geekmind.budgie.model.mapper.Mapper;
import io.geekmind.budgie.model.mapper.NewAccountToAccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final Mapper<NewAccount, Account> newAccountMapper;
    private final Mapper<Account, ExistingAccount> existingAccountMapper;

    @Autowired
    public AccountService(AccountRepository accountRepository,
                          @Qualifier(NewAccountToAccountMapper.QUALIFIER)Mapper<NewAccount, Account> newAccountMapper,
                          @Qualifier(AccountToExistingAccountMapper.QUALIFIER)Mapper<Account, ExistingAccount> existingAccountMapper) {
        this.accountRepository = accountRepository;
        this.newAccountMapper = newAccountMapper;
        this.existingAccountMapper = existingAccountMapper;
    }

    public ExistingAccount create(NewAccount account) {
        Account newAccount = this.newAccountMapper.mapTo(account);
        newAccount.setMainAccount(Boolean.FALSE);
        Account persistedAccount = this.accountRepository.save(newAccount);
        return this.existingAccountMapper.mapTo(persistedAccount);
    }

    public List<ExistingAccount> loadAll() {
        return this.accountRepository.findAll(
                Sort.by(
                    Sort.Order.desc("mainAccount"),
                    Sort.Order.asc("name")
                )
            ).stream()
            .map(this.existingAccountMapper::mapTo)
            .collect(Collectors.toList());
    }

    public Optional<ExistingAccount> remove(Integer id) {
        return this.accountRepository.findById(id)
            .map(account -> {
                this.accountRepository.delete(account);
                return this.existingAccountMapper.mapTo(account);
            });
    }

    public Optional<ExistingAccount> getMainAccount() {
        return this.accountRepository
            .findMainAccount()
            .map(this.existingAccountMapper::mapTo);
    }

    public Optional<ExistingAccount> loadById(Integer accountId) {
        return this.accountRepository.findById(accountId)
            .map(this.existingAccountMapper::mapTo);
    }

    public List<ExistingAccount> loadDependantAccounts() {
        return this.accountRepository.findDependantAccounts()
            .stream()
            .map(this.existingAccountMapper::mapTo)
            .collect(Collectors.toList());
    }
}
