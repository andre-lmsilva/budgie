package io.geekmind.budgie.repository;

import io.geekmind.budgie.model.dto.ExistingAccount;
import io.geekmind.budgie.model.dto.NewAccount;
import io.geekmind.budgie.model.entity.Account;
import io.geekmind.budgie.validation.UniquenessValidationService;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
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
        return this.accountRepository.findAll()
            .stream()
            .map(account -> this.mapper.map(account, ExistingAccount.class))
            .sorted(Comparator.comparing(ExistingAccount::getName))
            .sorted((accountA, accountB) -> Boolean.compare(accountB.getMainAccount(), accountA.getMainAccount()))
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
            .sorted(Comparator.comparing(ExistingAccount::getName))
            .collect(Collectors.toList());
    }

    @Override
    public Boolean canValidate(Class<?> type) {
        return type.equals(NewAccount.class) || type.equals(ExistingAccount.class);
    }

    @Override
    public Boolean isValid(Object entity) {
        if (entity instanceof NewAccount) {
            return this.isValidToCreate((NewAccount) entity);
        } else if (entity instanceof ExistingAccount) {
            return this.isValidToUpdate((ExistingAccount) entity);
        } else {
            return false;
        }
    }

    protected Boolean isValidToCreate(NewAccount entity) {
         return this.accountRepository
            .findAll()
            .stream()
            .noneMatch(nAccount -> nAccount.getName().equals(entity.getName()));
    }

    protected Boolean isValidToUpdate(ExistingAccount entity) {
          return this.accountRepository
              .findAll()
              .stream()
              .filter(account -> !account.getId().equals(entity.getId()))
              .noneMatch(nAccount -> nAccount.getName().equals(entity.getName()));
    }

    public List<ExistingAccount> loadNonDependantAccounts() {
        return this.accountRepository.findNonDependantAccounts()
            .stream()
            .map(account -> this.mapper.map(account, ExistingAccount.class))
            .sorted(Comparator.comparing(ExistingAccount::getName))
            .collect(Collectors.toList());
    }

    public ExistingAccount update(ExistingAccount existingAccount) {
        Optional<Account> accountToUpdate = this.accountRepository.findById(existingAccount.getId());
        if (accountToUpdate.isPresent()) {
            Account account = accountToUpdate.get();
            this.mapper.map(existingAccount, account);
            this.accountRepository.save(account);
        }
        return existingAccount;
    }
}
