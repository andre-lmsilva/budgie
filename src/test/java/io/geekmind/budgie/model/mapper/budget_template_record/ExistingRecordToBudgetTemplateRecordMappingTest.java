package io.geekmind.budgie.model.mapper.budget_template_record;

import io.geekmind.budgie.fixture.ExistingRecordFixture;
import io.geekmind.budgie.model.entity.BudgetTemplateRecord;
import io.geekmind.budgie.model.mapper.BudgetTemplateRecordMappingSettings;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class ExistingRecordToBudgetTemplateRecordMappingTest {

    private BudgetTemplateRecord resultBudgetTemplateRecord;

    @Before
    public void setUp() {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        new BudgetTemplateRecordMappingSettings().configure(mapperFactory);
        this.resultBudgetTemplateRecord = mapperFactory.getMapperFacade()
            .map(
                ExistingRecordFixture.getWithValue(BigDecimal.TEN),
                BudgetTemplateRecord.class
            );
    }

    @Test
    public void allAttributes_areNotMapped() {
        assertThat(this.resultBudgetTemplateRecord)
            .hasFieldOrPropertyWithValue("id", null)
            .hasFieldOrPropertyWithValue("account", null)
            .hasFieldOrPropertyWithValue("category", null)
            .hasFieldOrPropertyWithValue("recordDate", null)
            .hasFieldOrPropertyWithValue("description", null)
            .hasFieldOrPropertyWithValue("recordValue", null)
            .hasFieldOrPropertyWithValue("recordContainer", null);

    }

}
