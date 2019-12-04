package io.geekmind.budgie.model.mapper.budget_record;

import io.geekmind.budgie.fixture.BudgetRecordFixture;
import io.geekmind.budgie.model.dto.ExistingRecord;
import io.geekmind.budgie.model.dto.standard_account.ExistingStandardAccount;
import io.geekmind.budgie.model.entity.BudgetRecord;
import io.geekmind.budgie.model.mapper.BudgetRecordMappingSettings;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BudgetRecordToExistingRecordMappingTest {

    private BudgetRecord sourceBudgetRecord;
    private ExistingRecord resultExistingRecord;

    @Before
    public void setUp() {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        new BudgetRecordMappingSettings().configure(mapperFactory);
        this.sourceBudgetRecord = BudgetRecordFixture.salary();
        this.resultExistingRecord = mapperFactory.getMapperFacade()
            .map(this.sourceBudgetRecord, ExistingRecord.class);
    }

    @Test
    public void idAttribute_isMapped() {
        assertThat(this.resultExistingRecord)
            .hasFieldOrPropertyWithValue("id", this.sourceBudgetRecord.getId());
    }

    @Test
    public void accountAttribute_isMapped() {
        assertThat(this.resultExistingRecord.getAccount())
            .isInstanceOf(ExistingStandardAccount.class)
            .hasFieldOrPropertyWithValue("id", this.sourceBudgetRecord.getAccount().getId());
    }

    @Test
    public void categoryAttribute_isMapped() {
        assertThat(this.resultExistingRecord.getCategory())
            .isEqualToComparingFieldByField(this.sourceBudgetRecord.getCategory());
    }

    @Test
    public void recordDateAttribute_isMapped() {
        assertThat(this.resultExistingRecord)
            .hasFieldOrPropertyWithValue("recordDate", this.sourceBudgetRecord.getRecordDate());
    }

    @Test
    public void descriptionAttribute_isMapped() {
        assertThat(this.resultExistingRecord)
            .hasFieldOrPropertyWithValue("description", this.sourceBudgetRecord.getDescription());
    }

    @Test
    public void recordValueAttribute_isMapped() {
        assertThat(this.resultExistingRecord)
            .hasFieldOrPropertyWithValue("recordValue", this.sourceBudgetRecord.getRecordValue());
    }

    @Test
    public void containerIdAttribute_isMapped() {
        assertThat(this.resultExistingRecord)
            .hasFieldOrPropertyWithValue("containerId", this.sourceBudgetRecord.getRecordContainer().getId());
    }

    @Test
    public void recordTypeAttribute_isFilledIn() {
        assertThat(this.resultExistingRecord)
            .hasFieldOrPropertyWithValue("recordType", BudgetRecord.class.getSimpleName());
    }

}
