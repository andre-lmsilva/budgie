package io.geekmind.budgie.repository;

import io.geekmind.budgie.model.dto.standard_account.EditStandardAccount;
import io.geekmind.budgie.model.dto.standard_account.ExistingStandardAccount;
import io.geekmind.budgie.model.dto.standard_account.NewStandardAccount;
import io.geekmind.budgie.model.entity.Account;
import io.geekmind.budgie.model.entity.StandardAccount;
import io.geekmind.budgie.validation.UniquenessValidationService;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StandardAccountService implements UniquenessValidationService {

    private final StandardAccountRepository standardAccountRepository;
    private final MapperFacade mapper;

    @Autowired
    public StandardAccountService(StandardAccountRepository standardAccountRepository,
                                  MapperFacade mapper) {
        this.standardAccountRepository = standardAccountRepository;
        this.mapper = mapper;
    }

    public ExistingStandardAccount create(NewStandardAccount account) {
        StandardAccount newAccount = this.mapper.map(account, StandardAccount.class);
        newAccount.setMainAccount(Boolean.FALSE);
        Account persistedAccount = this.standardAccountRepository.save(newAccount);
        return this.mapper.map(persistedAccount, ExistingStandardAccount.class);
    }

    public List<ExistingStandardAccount> loadAll() {
        return this.standardAccountRepository.findByArchivedFalse()
            .stream()
            .map(account -> this.mapper.map(account, ExistingStandardAccount.class))
            .sorted(Comparator.comparing(ExistingStandardAccount::getName))
            .sorted((accountA, accountB) -> Boolean.compare(accountB.getMainAccount(), accountA.getMainAccount()))
            .collect(Collectors.toList());
    }

    public List<ExistingStandardAccount> loadAllExcept(Integer id) {
        return this.loadAll()
            .stream()
            .filter(account -> !account.getId().equals(id))
            .collect(Collectors.toList());
    }

    public Optional<ExistingStandardAccount> remove(Integer id) {
        return this.standardAccountRepository.findById(id)
            .map(account -> {
                account.setArchived(Boolean.TRUE);
                this.standardAccountRepository.save(account);
                return this.mapper.map(account, ExistingStandardAccount.class);
            });
    }

    public Optional<ExistingStandardAccount> getMainAccount() {
        return this.standardAccountRepository
            .findMainAccount()
            .map(account -> this.mapper.map(account, ExistingStandardAccount.class));
    }

    public Optional<ExistingStandardAccount> loadById(Integer accountId) {
        return this.standardAccountRepository.findById(accountId)
            .map(account -> this.mapper.map(account, ExistingStandardAccount.class));
    }

    @Override
    public Boolean canValidate(Class<?> type) {
        return type.equals(NewStandardAccount.class) || type.equals(EditStandardAccount.class);
    }

    @Override
    public Boolean isValid(Object entity) {
        if (entity instanceof NewStandardAccount) {
            return this.isValidToCreate((NewStandardAccount) entity);
        } else if (entity instanceof EditStandardAccount) {
            return this.isValidToUpdate((EditStandardAccount) entity);
        } else {
            return false;
        }
    }

    protected Boolean isValidToCreate(NewStandardAccount entity) {
         return this.standardAccountRepository
            .findAll()
            .stream()
            .noneMatch(nAccount -> nAccount.getName().equals(entity.getName()));
    }

    protected Boolean isValidToUpdate(EditStandardAccount entity) {
          return this.standardAccountRepository
              .findAll()
              .stream()
              .filter(account -> !account.getId().equals(entity.getId()))
              .noneMatch(nAccount -> nAccount.getName().equals(entity.getName()));
    }

    public List<ExistingStandardAccount> loadNonDependantAccounts() {
        return this.standardAccountRepository.findNonDependantAccounts()
            .stream()
            .map(account -> this.mapper.map(account, ExistingStandardAccount.class))
            .sorted(Comparator.comparing(ExistingStandardAccount::getName))
            .collect(Collectors.toList());
    }

    public EditStandardAccount update(EditStandardAccount editAccount) {
        Optional<StandardAccount> accountToUpdate = this.standardAccountRepository.findById(editAccount.getId());
        if (accountToUpdate.isPresent()) {
            StandardAccount account = accountToUpdate.get();
            account.setParent(null);
            this.mapper.map(editAccount, account);

            if (null != editAccount.getParentId()) {
                account.setParent(
                    this.standardAccountRepository.findById(editAccount.getParentId()).orElse(null)
                );
            }
            this.standardAccountRepository.save(account);
        }
        return editAccount;
    }

    public Optional<EditStandardAccount> loadByIdForEdit(Integer id) {
        return this.standardAccountRepository.findById(id)
            .map(account ->  this.mapper.map(account, EditStandardAccount.class));
    }
}
