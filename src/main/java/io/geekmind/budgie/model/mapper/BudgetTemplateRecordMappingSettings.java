package io.geekmind.budgie.model.mapper;

import io.geekmind.budgie.model.dto.ExistingRecord;
import io.geekmind.budgie.model.dto.budget_template_record.NewBudgetTemplateRecord;
import io.geekmind.budgie.model.entity.BudgetTemplateRecord;
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
                .mapNulls(false)
                .mapNullsInReverse(false)
                .byDefault()
                .register();

    }
}
