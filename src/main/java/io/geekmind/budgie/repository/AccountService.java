package io.geekmind.budgie.repository;

import io.geekmind.budgie.model.dto.account.EditAccount;
import io.geekmind.budgie.model.dto.account.ExistingAccount;
import io.geekmind.budgie.model.dto.account.NewAccount;
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

    public List<ExistingAccount> loadAllExcept(Integer id) {
        return this.loadAll()
            .stream()
            .filter(account -> !account.getId().equals(id))
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
        return type.equals(NewAccount.class) || type.equals(EditAccount.class);
    }

    @Override
    public Boolean isValid(Object entity) {
        if (entity instanceof NewAccount) {
            return this.isValidToCreate((NewAccount) entity);
        } else if (entity instanceof EditAccount) {
            return this.isValidToUpdate((EditAccount) entity);
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

    protected Boolean isValidToUpdate(EditAccount entity) {
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

    public EditAccount update(EditAccount editAccount) {
        Optional<Account> accountToUpdate = this.accountRepository.findById(editAccount.getId());
        if (accountToUpdate.isPresent()) {
            Account account = accountToUpdate.get();
            account.setParent(null);
            this.mapper.map(editAccount, account);

            if (null != editAccount.getParentId()) {
                account.setParent(
                    this.accountRepository.findById(editAccount.getParentId()).orElse(null)
                );
            }
            this.accountRepository.save(account);
        }
        return editAccount;
    }

    public Optional<EditAccount> loadByIdForEdit(Integer id) {
        return this.accountRepository.findById(id)
            .map(account ->  this.mapper.map(account, EditAccount.class));
    }
}
