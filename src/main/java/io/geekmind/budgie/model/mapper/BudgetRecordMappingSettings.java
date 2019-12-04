package io.geekmind.budgie.model.mapper;

import io.geekmind.budgie.model.dto.ExistingRecord;
import io.geekmind.budgie.model.entity.BudgetRecord;
import io.geekmind.budgie.model.entity.BudgetTemplateRecord;
import io.geekmind.budgie.model.entity.BudgetTemplateRecordContainer;
import io.geekmind.budgie.model.entity.StandardAccount;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import net.rakugakibox.spring.boot.orika.OrikaMapperFactoryConfigurer;
import org.springframework.stereotype.Component;

@Component
public class BudgetRecordMappingSettings implements OrikaMapperFactoryConfigurer {
    @Override
    public void configure(MapperFactory orikaMapperFactory) {
        orikaMapperFactory.classMap(BudgetRecord.class, ExistingRecord.class)
            .fieldAToB("id", "id")
            .fieldAToB("recordDate", "recordDate")
            .fieldAToB("account", "account")
            .fieldAToB("category", "category")
            .fieldAToB("description", "description")
            .fieldAToB("recordValue", "recordValue")
            .customize(new CustomMapper<BudgetRecord, ExistingRecord>() {
                @Override
                public void mapAtoB(BudgetRecord budgetRecord, ExistingRecord existingRecord, MappingContext context) {
                    super.mapAtoB(budgetRecord, existingRecord, context);
                    existingRecord.setContainerId(budgetRecord.getRecordContainer().getId());
                    existingRecord.setRecordType(BudgetRecord.class.getSimpleName());
                }
            })
            .register();

        orikaMapperFactory.classMap(ExistingRecord.class, BudgetRecord.class)
                .fieldAToB("category.id", "category.id")
                .fieldAToB("description", "description")
                .fieldAToB("recordValue", "recordValue")
                .customize(new CustomMapper<ExistingRecord, BudgetRecord>() {
                    @Override
                    public void mapAtoB(ExistingRecord existingRecord, BudgetRecord budgetRecord, MappingContext context) {
                        super.mapAtoB(existingRecord, budgetRecord, context);
                        budgetRecord.setAccount(new StandardAccount());
                        budgetRecord.getAccount().setId(existingRecord.getAccount().getId());

                        budgetRecord.setRecordContainer(new BudgetTemplateRecordContainer());
                        budgetRecord.getRecordContainer().setId(existingRecord.getContainerId());
                    }
                })
                .register();

    }
}
