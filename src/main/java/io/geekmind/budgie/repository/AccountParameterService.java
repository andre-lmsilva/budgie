package io.geekmind.budgie.repository;

import io.geekmind.budgie.model.dto.account.ExistingAccount;
import io.geekmind.budgie.model.dto.account_parameter.ExistingAccountParameter;
import io.geekmind.budgie.model.dto.project_account.ExistingProjectAccount;
import io.geekmind.budgie.model.dto.standard_account.ExistingStandardAccount;
import io.geekmind.budgie.model.entity.AccountParameter;
import io.geekmind.budgie.model.entity.AccountParameterKey;
import io.geekmind.budgie.model.entity.ProjectAccount;
import io.geekmind.budgie.model.entity.StandardAccount;
import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountParameterService {

    private final MapperFacade mapperFace;
    private final AccountParameterRepository accountParameterRepository;

    public AccountParameterService(MapperFacade mapperFacade,
                                   AccountParameterRepository accountParameterRepository) {
        this.mapperFace = mapperFacade;
        this.accountParameterRepository = accountParameterRepository;
    }

    public Optional<ExistingAccountParameter> loadByAccountAndKey(ExistingAccount existingAccount, AccountParameterKey key) {
        return this.accountParameterRepository
            .findByAccount_Id(existingAccount.getId())
            .stream()
            .filter(parameter -> key.name().equals(parameter.getKey()))
            .findFirst()
            .map(parameter -> this.mapperFace.map(parameter, ExistingAccountParameter.class));
    }

    public Optional<ExistingAccountParameter> upsert(ExistingAccount existingAccount, AccountParameterKey key, String value) {
        return this.loadByAccountAndKey(existingAccount, key)
            .map(existingAccountParameter -> updateParameter(existingAccountParameter.getId(), value))
            .orElseGet(() -> createParameter(existingAccount, key, value));
    }

    public Optional<ExistingAccountParameter> createParameter(ExistingAccount existingAccount, AccountParameterKey key, String value) {
        AccountParameter accountParameter = new AccountParameter();

        if (existingAccount instanceof ExistingStandardAccount) {
            accountParameter.setAccount(new StandardAccount());
        } else if (existingAccount instanceof ExistingProjectAccount) {
            accountParameter.setAccount(new ProjectAccount());
        }
        accountParameter.getAccount().setId(existingAccount.getId());
        accountParameter.setKey(key.name());
        accountParameter.setValue(value);

        return Optional.of(
            this.mapperFace.map(
                this.accountParameterRepository.save(accountParameter),
                ExistingAccountParameter.class
            )
        );
    }

    public Optional<ExistingAccountParameter> updateParameter(Integer id, String value) {
        return this.accountParameterRepository.findById(id)
            .map(parameter -> {
                parameter.setValue(value);
                return this.mapperFace.map(
                        this.accountParameterRepository.save(parameter),
                        ExistingAccountParameter.class
                );
            });
    }

}
