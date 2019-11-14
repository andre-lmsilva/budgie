package io.geekmind.budgie.repository;

import io.geekmind.budgie.model.dto.project_account.EditProjectAccount;
import io.geekmind.budgie.model.dto.project_account.ExistingProjectAccount;
import io.geekmind.budgie.model.dto.project_account.NewProjectAccount;
import io.geekmind.budgie.model.dto.standard_account.ExistingStandardAccount;
import io.geekmind.budgie.model.entity.ProjectAccount;
import io.geekmind.budgie.validation.UniquenessValidationService;
import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectAccountService implements UniquenessValidationService {

    private final MapperFacade mapperFacade;
    private final ProjectAccountRepository projectAccountRepository;
    private final StandardAccountService standardAccountService;

    public ProjectAccountService(MapperFacade mapperFacade,
                                 ProjectAccountRepository projectAccountRepository,
                                 StandardAccountService standardAccountService) {
        this.mapperFacade = mapperFacade;
        this.projectAccountRepository = projectAccountRepository;
        this.standardAccountService = standardAccountService;
    }

    public List<ExistingProjectAccount> loadAll() {
        return this.projectAccountRepository.findByArchivedFalse()
            .stream()
            .map(projectAccount -> this.mapperFacade.map(projectAccount, ExistingProjectAccount.class))
            .sorted(Comparator.comparing(ExistingProjectAccount::getName))
            .collect(Collectors.toList());
    }

    public Optional<ExistingProjectAccount> create(NewProjectAccount newProjectAccount) {
        return this.standardAccountService.loadById(newProjectAccount.getParentId())
            .map(parentAccount -> {
                ProjectAccount projectAccount = this.mapperFacade.map(newProjectAccount, ProjectAccount.class);
                this.mapperFacade.map(parentAccount, projectAccount);
                projectAccount.setShowBalanceOnParentAccount(Boolean.TRUE);
                this.projectAccountRepository.save(projectAccount);
                return this.mapperFacade.map(projectAccount, ExistingProjectAccount.class);
            });
    }

    @Override
    public Boolean canValidate(Class<?> type) {
        return type.equals(NewProjectAccount.class) || type.equals(EditProjectAccount.class);
    }

    @Override
    public Boolean isValid(Object entity) {
        if (entity instanceof NewProjectAccount) {
            return this.isValidToCreate((NewProjectAccount) entity);
        } else if (entity instanceof EditProjectAccount) {
            return this.isValidToUpdate((EditProjectAccount) entity);
        }
        return false;
    }

    protected boolean isValidToCreate(NewProjectAccount newProjectAccount) {
        return this.projectAccountRepository.findAll()
            .stream()
            .filter(account -> !account.getArchived())
            .noneMatch(account -> account.getName().equals(newProjectAccount.getName()));
    }

    protected boolean isValidToUpdate(EditProjectAccount editProjectAccount) {
        return this.projectAccountRepository.findAll()
            .stream()
            .filter(account -> !account.getArchived())
            .filter(account -> !account.getId().equals(editProjectAccount.getId()))
            .noneMatch(account -> account.getName().equals(editProjectAccount.getName()));
    }

    public Optional<EditProjectAccount> loadByIdForEdit(Integer id) {
        return this.projectAccountRepository.findById(id)
            .map(projectAccount -> this.mapperFacade.map(projectAccount, EditProjectAccount.class));
    }

    public Optional<ExistingProjectAccount> update(EditProjectAccount editProjectAccount) {
        return this.projectAccountRepository.findById(editProjectAccount.getId())
            .map(projectAccount -> {

                projectAccount.setParent(null);
                this.mapperFacade.map(editProjectAccount, projectAccount);

                Optional<ExistingStandardAccount> parentAccount = this.standardAccountService.loadById(projectAccount.getParent().getId());
                if (parentAccount.isPresent()) {
                    this.mapperFacade.map(parentAccount, projectAccount);
                }

                projectAccount.setShowBalanceOnParentAccount(Boolean.TRUE);
                this.projectAccountRepository.save(projectAccount);
                return this.mapperFacade.map(projectAccount, ExistingProjectAccount.class);
            });
    }

}
