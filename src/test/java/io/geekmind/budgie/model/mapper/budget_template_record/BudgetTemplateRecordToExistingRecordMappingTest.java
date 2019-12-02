package io.geekmind.budgie.model.mapper.budget_template_record;

import io.geekmind.budgie.fixture.BudgetTemplateRecordFixture;
import io.geekmind.budgie.model.dto.ExistingRecord;
import io.geekmind.budgie.model.entity.BudgetTemplateRecord;
import io.geekmind.budgie.model.mapper.BudgetTemplateRecordMappingSettings;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class BudgetTemplateRecordToExistingRecordMappingTest {

    private BudgetTemplateRecord sourceBudgetTemplateRecord;
    private ExistingRecord resultBudgetTemplateRecord;

    @Before
    public void setUp() {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        new BudgetTemplateRecordMappingSettings().configure(mapperFactory);
        this.sourceBudgetTemplateRecord = BudgetTemplateRecordFixture.rent();
        this.resultBudgetTemplateRecord = mapperFactory.getMapperFacade()
            .map(this.sourceBudgetTemplateRecord, ExistingRecord.class);
    }

    @Test
    public void idAttribute_isMapped() {
        assertThat(this.resultBudgetTemplateRecord)
            .hasFieldOrPropertyWithValue("id", this.sourceBudgetTemplateRecord.getId());
    }

}
