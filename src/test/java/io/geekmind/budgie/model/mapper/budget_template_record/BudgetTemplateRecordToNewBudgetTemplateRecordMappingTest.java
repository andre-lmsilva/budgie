package io.geekmind.budgie.model.mapper.budget_template_record;

import io.geekmind.budgie.fixture.BudgetTemplateRecordFixture;
import io.geekmind.budgie.model.dto.budget_template_record.NewBudgetTemplateRecord;
import io.geekmind.budgie.model.mapper.BudgetTemplateRecordMappingSettings;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BudgetTemplateRecordToNewBudgetTemplateRecordMappingTest {

    private NewBudgetTemplateRecord resultNewBudgetTemplateRecord;

    @Before
    public void setUp() {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        new BudgetTemplateRecordMappingSettings().configure(mapperFactory);
        this.resultNewBudgetTemplateRecord = mapperFactory.getMapperFacade()
            .map(BudgetTemplateRecordFixture.rent(), NewBudgetTemplateRecord.class);
    }

    @Test
    public void allAttributes_areNotMapped() {
        assertThat(this.resultNewBudgetTemplateRecord)
            .hasFieldOrPropertyWithValue("dayOfMonth", null)
            .hasFieldOrPropertyWithValue("accountId", null)
            .hasFieldOrPropertyWithValue("categoryId", null)
            .hasFieldOrPropertyWithValue("description", null)
            .hasFieldOrPropertyWithValue("recordValue", null);
    }

}
