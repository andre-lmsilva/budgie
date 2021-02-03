package io.geekmind.budgie.model.mapper;

import io.geekmind.budgie.model.dto.ExistingRecord;
import io.geekmind.budgie.model.dto.budget_template_record.NewBudgetTemplateRecord;
import io.geekmind.budgie.model.entity.BudgetRecord;
import io.geekmind.budgie.model.entity.BudgetTemplateRecord;
import io.geekmind.budgie.model.entity.BudgetTemplateRecordContainer;
import io.geekmind.budgie.model.entity.StandardAccount;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import net.rakugakibox.spring.boot.orika.OrikaMapperFactoryConfigurer;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class BudgetTemplateRecordMappingSettings implements OrikaMapperFactoryConfigurer {
    @Override
    public void configure(MapperFactory orikaMapperFactory) {
        orikaMapperFactory.classMap(NewBudgetTemplateRecord.class, BudgetTemplateRecord.class)
                .fieldAToB("accountId", "account.id")
                .fieldAToB("categoryId", "category.id")
                .fieldAToB("description", "description")
                .fieldAToB("recordValue", "recordValue")
                .fieldAToB("isTaxRefundable", "isTaxRefundable")
                .customize(new CustomMapper<NewBudgetTemplateRecord, BudgetTemplateRecord>() {
                    @Override
                    public void mapAtoB(NewBudgetTemplateRecord newBudgetTemplateRecord, BudgetTemplateRecord budgetTemplateRecord, MappingContext context) {
                        super.mapAtoB(newBudgetTemplateRecord, budgetTemplateRecord, context);
                        LocalDate recordDate = LocalDate.of(9999, 12, newBudgetTemplateRecord.getDayOfMonth());
                        budgetTemplateRecord.setRecordDate(recordDate);
                    }
                })
                .register();


        orikaMapperFactory.classMap(BudgetTemplateRecord.class, ExistingRecord.class)
                .fieldAToB("id", "id")
                .fieldAToB("account", "account")
                .fieldAToB("category", "category")
                .fieldAToB("recordDate", "recordDate")
                .fieldAToB("description", "description")
                .fieldAToB("recordValue", "recordValue")
                .fieldAToB("recordContainer.id", "containerId")
                .fieldAToB("isTaxRefundable", "isTaxRefundable")
                .customize(new CustomMapper<BudgetTemplateRecord, ExistingRecord>() {
                    @Override
                    public void mapAtoB(BudgetTemplateRecord budgetTemplateRecord, ExistingRecord existingRecord, MappingContext context) {
                        super.mapAtoB(budgetTemplateRecord, existingRecord, context);
                        existingRecord.setRecordType(BudgetTemplateRecord.class.getSimpleName());
                    }
                })
                .register();



    }
}
