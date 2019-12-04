package io.geekmind.budgie.model.mapper.budget_template_record;

import io.geekmind.budgie.fixture.BudgetTemplateRecordFixture;
import io.geekmind.budgie.model.dto.ExistingRecord;
import io.geekmind.budgie.model.dto.standard_account.ExistingStandardAccount;
import io.geekmind.budgie.model.entity.BudgetTemplateRecord;
import io.geekmind.budgie.model.mapper.BudgetTemplateRecordMappingSettings;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class BudgetTemplateRecordToExistingRecordMappingTest {

    private BudgetTemplateRecord sourceBudgetTemplateRecord;
    private ExistingRecord resultExistingRecord;

    @Before
    public void setUp() {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        new BudgetTemplateRecordMappingSettings().configure(mapperFactory);
        this.sourceBudgetTemplateRecord = BudgetTemplateRecordFixture.rent();
        this.resultExistingRecord = mapperFactory.getMapperFacade()
            .map(this.sourceBudgetTemplateRecord, ExistingRecord.class);
    }

    @Test
    public void idAttribute_isMapped() {
        assertThat(this.resultExistingRecord)
            .hasFieldOrPropertyWithValue("id", this.sourceBudgetTemplateRecord.getId());
    }

    @Test
    public void accountAttribute_isMapped() {
        assertThat(this.resultExistingRecord.getAccount())
            .isInstanceOf(ExistingStandardAccount.class)
            .hasFieldOrPropertyWithValue("id", this.sourceBudgetTemplateRecord.getAccount().getId());
    }

    @Test
    public void categoryAttribute_isMapped() {
        assertThat(this.resultExistingRecord.getCategory())
            .hasFieldOrPropertyWithValue("id", this.sourceBudgetTemplateRecord.getCategory().getId());
    }

    @Test
    public void recordDateAttribute_isMapped() {
        assertThat(this.resultExistingRecord)
            .hasFieldOrPropertyWithValue("recordDate", this.sourceBudgetTemplateRecord.getRecordDate());
    }

    @Test
    public void descriptionAttribute_isMapped() {
        assertThat(this.resultExistingRecord)
            .hasFieldOrPropertyWithValue("description", this.sourceBudgetTemplateRecord.getDescription());
    }

    @Test
    public void recordValueAttribute_isMapped() {
        assertThat(this.resultExistingRecord)
            .hasFieldOrPropertyWithValue("recordValue", this.sourceBudgetTemplateRecord.getRecordValue());
    }

    @Test
    public void containerIdAttribute_isMapped() {
        assertThat(this.resultExistingRecord)
            .hasFieldOrPropertyWithValue("containerId", this.sourceBudgetTemplateRecord.getRecordContainer().getId());
    }

    @Test
    public void recordTypeAttribute_isFilledWithBudgetTemplateRecordSimpleClassName() {
        assertThat(this.resultExistingRecord)
            .hasFieldOrPropertyWithValue("recordType", BudgetTemplateRecord.class.getSimpleName());
    }

}
