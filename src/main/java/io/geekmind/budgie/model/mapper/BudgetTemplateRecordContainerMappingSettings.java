package io.geekmind.budgie.model.mapper;

import io.geekmind.budgie.model.dto.NewBudgetTemplateRecord;
import io.geekmind.budgie.model.entity.BudgetTemplateRecordContainer;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import net.rakugakibox.spring.boot.orika.OrikaMapperFactoryConfigurer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class BudgetTemplateRecordContainerMappingSettings implements OrikaMapperFactoryConfigurer {
    @Override
    public void configure(MapperFactory orikaMapperFactory) {
        orikaMapperFactory.classMap(NewBudgetTemplateRecord.class, BudgetTemplateRecordContainer.class)
            .customize(new CustomMapper<NewBudgetTemplateRecord, BudgetTemplateRecordContainer>() {
                @Override
                public void mapAtoB(NewBudgetTemplateRecord newBudgetTemplateRecord, BudgetTemplateRecordContainer budgetTemplateRecordContainer, MappingContext context) {
                    super.mapAtoB(newBudgetTemplateRecord, budgetTemplateRecordContainer, context);

                    String name = String.format(
                        "Account id: %s / Category id: %s / Day of Month: %s / Description: %s",
                        newBudgetTemplateRecord.getAccountId(),
                        newBudgetTemplateRecord.getCategoryId(),
                        newBudgetTemplateRecord.getDayOfMonth(),
                        newBudgetTemplateRecord.getDescription()
                    );
                    budgetTemplateRecordContainer.setName(name);
                    budgetTemplateRecordContainer.setRecords(new ArrayList<>());
                }
            }).register();
    }
}
