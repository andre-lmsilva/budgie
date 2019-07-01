package io.geekmind.budgie.repository;

import io.geekmind.budgie.model.dto.ExistingAccount;
import io.geekmind.budgie.model.dto.NewAccount;
import io.geekmind.budgie.model.entity.Account;
import io.geekmind.budgie.validation.UniquenessValidationService;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountService implements UniquenessValidationService {

    private final AccountRepository accountRepository;
    private final MapperFacade mapper;

    @Autowired
    public AccountService(AccountRepository accountRepository,
                          MapperFacade mapper) {
        this.accountRepository = accountRepository;
        this.mapper = mapper;
    }

    public ExistingAccount create(NewAccount account) {
        Account newAccount = this.mapper.map(account, Account.class);
        newAccount.setMainAccount(Boolean.FALSE);
        Account persistedAccount = this.accountRepository.save(newAccount);
        return this.mapper.map(persistedAccount, ExistingAccount.class);
    }

    public List<ExistingAccount> loadAll() {
        return this.accountRepository.findAll(
                Sort.by(
                    Sort.Order.desc("mainAccount"),
                    Sort.Order.asc("name")
                )
            ).stream()
            .map(account -> this.mapper.map(account, ExistingAccount.class))
            .collect(Collectors.toList());
    }

    public Optional<ExistingAccount> remove(Integer id) {
        return this.accountRepository.findById(id)
            .map(account -> {
                this.accountRepository.delete(account);
                return this.mapper.map(account, ExistingAccount.class);
            });
    }

    public Optional<ExistingAccount> getMainAccount() {
        return this.accountRepository
            .findMainAccount()
            .map(account -> this.mapper.map(account, ExistingAccount.class));
    }

    public Optional<ExistingAccount> loadById(Integer accountId) {
        return this.accountRepository.findById(accountId)
            .map(account -> this.mapper.map(account, ExistingAccount.class));
    }

    public List<ExistingAccount> loadDependantAccounts() {
        return this.accountRepository.findDependantAccounts()
            .stream()
            .map(account -> this.mapper.map(account, ExistingAccount.class))
            .collect(Collectors.toList());
    }

    @Override
    public Boolean canValidate(Class<?> type) {
        return type.equals(NewAccount.class);
    }

    @Override
    public Boolean isValid(Object entity) {
        NewAccount account = (NewAccount) entity;
        return !this.accountRepository.findByName(account.getName()).isPresent();
    }

    public List<ExistingAccount> loadNonDependantAccounts() {
        return this.accountRepository.findNonDependantAccounts()
            .stream()
            .map(account -> this.mapper.map(account, ExistingAccount.class))
            .collect(Collectors.toList());
    }

}
