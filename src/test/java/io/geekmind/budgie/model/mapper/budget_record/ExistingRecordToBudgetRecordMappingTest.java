package io.geekmind.budgie.model.mapper.budget_record;

import io.geekmind.budgie.fixture.ExistingRecordFixture;
import io.geekmind.budgie.model.dto.ExistingRecord;
import io.geekmind.budgie.model.entity.BudgetRecord;
import io.geekmind.budgie.model.entity.BudgetTemplateRecordContainer;
import io.geekmind.budgie.model.entity.StandardAccount;
import io.geekmind.budgie.model.mapper.BudgetRecordMappingSettings;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ExistingRecordToBudgetRecordMappingTest {

    private BudgetRecord resultBudgetRecord;
    private ExistingRecord sourceExistingRecord;

    @Before
    public void setUp() {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        new BudgetRecordMappingSettings().configure(mapperFactory);
        this.sourceExistingRecord = ExistingRecordFixture.getBudgetTemplateRecord();
        this.resultBudgetRecord = mapperFactory.getMapperFacade()
            .map(this.sourceExistingRecord, BudgetRecord.class);
    }

    @Test
    public void idAttribute_isNotMapped() {
        assertThat(this.resultBudgetRecord)
            .hasFieldOrPropertyWithValue("id", null);
    }

    @Test
    public void recordDateAttribute_isNotMapped() {
        assertThat(this.resultBudgetRecord)
            .hasFieldOrPropertyWithValue("recordDate", null);
    }

    @Test
    public void accountAttribute_isMapped() {
        assertThat(this.resultBudgetRecord.getAccount())
            .isInstanceOf(StandardAccount.class)
            .hasFieldOrPropertyWithValue("id", this.sourceExistingRecord.getAccount().getId());
    }

    @Test
    public void categoryAttribute_isMapped() {
        assertThat(this.resultBudgetRecord)
            .hasFieldOrPropertyWithValue("category.id", this.sourceExistingRecord.getCategory().getId());
    }

    @Test
    public void containerIdAttribute_isMapped() {
        assertThat(this.resultBudgetRecord.getRecordContainer())
            .isInstanceOf(BudgetTemplateRecordContainer.class)
            .hasFieldOrPropertyWithValue("id", this.sourceExistingRecord.getContainerId());
    }

    @Test
    public void descriptionAttribute_isMapped() {
        assertThat(this.resultBudgetRecord)
            .hasFieldOrPropertyWithValue("description", this.sourceExistingRecord.getDescription());
    }

    @Test
    public void recordValueAttribute_isMapped() {
        assertThat(this.resultBudgetRecord)
            .hasFieldOrPropertyWithValue("recordValue", this.sourceExistingRecord.getRecordValue());
    }

}
