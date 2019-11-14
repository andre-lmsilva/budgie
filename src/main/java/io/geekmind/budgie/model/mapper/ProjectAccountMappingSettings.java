package io.geekmind.budgie.model.mapper;

import io.geekmind.budgie.model.dto.project_account.EditProjectAccount;
import io.geekmind.budgie.model.dto.project_account.NewProjectAccount;
import io.geekmind.budgie.model.dto.standard_account.ExistingStandardAccount;
import io.geekmind.budgie.model.entity.ProjectAccount;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import net.rakugakibox.spring.boot.orika.OrikaMapperFactoryConfigurer;
import org.springframework.stereotype.Component;

@Component
public class ProjectAccountMappingSettings implements OrikaMapperFactoryConfigurer {
    @Override
    public void configure(MapperFactory orikaMapperFactory) {
        orikaMapperFactory.classMap(NewProjectAccount.class, ProjectAccount.class)
            .fieldAToB("name", "name")
            .fieldAToB("description", "description")
            .fieldAToB("parentId", "parent.id")
            .fieldAToB("targetValue", "targetValue")
            .register();

        orikaMapperFactory.classMap(ExistingStandardAccount.class, ProjectAccount.class)
            .fieldAToB("monthStartingAt", "monthStartingAt")
            .fieldAToB("monthBillingDayAt", "monthBillingDayAt")
            .fieldAToB("currency.currencyCode", "currencyCode")
            .customize(new CustomMapper<ExistingStandardAccount, ProjectAccount>() {
                @Override
                public void mapAtoB(ExistingStandardAccount standardAccount, ProjectAccount projectAccount, MappingContext context) {
                    super.mapAtoB(standardAccount, projectAccount, context);
                    projectAccount.setShowBalanceOnParentAccount(Boolean.TRUE);
                }
            })
            .register();

        orikaMapperFactory.classMap(ProjectAccount.class, EditProjectAccount.class)
            .fieldAToB("id", "id")
            .field("name", "name")
            .field("description", "description")
            .field("parent.id", "parentId")
            .field("targetValue", "targetValue")
            .register();

    }
}
