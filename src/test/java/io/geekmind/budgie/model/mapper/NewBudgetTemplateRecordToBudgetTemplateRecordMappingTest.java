package io.geekmind.budgie.model.mapper;

import io.geekmind.budgie.fixture.NewBudgetTemplateRecordFixture;
import io.geekmind.budgie.model.dto.budget_template_record.NewBudgetTemplateRecord;
import io.geekmind.budgie.model.entity.BudgetTemplateRecord;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class NewBudgetTemplateRecordToBudgetTemplateRecordMappingTest {

    private NewBudgetTemplateRecord sourceNewBudgetTemplateRecord;
    private BudgetTemplateRecord resultBudgetTemplateRecord;

    @Before
    public void setUp() {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        new BudgetTemplateRecordMappingSettings().configure(mapperFactory);
        this.sourceNewBudgetTemplateRecord = NewBudgetTemplateRecordFixture.salary();
        this.resultBudgetTemplateRecord = mapperFactory.getMapperFacade().map(
            this.sourceNewBudgetTemplateRecord,
            BudgetTemplateRecord.class
        );
    }

    @Test
    public void idAttribute_isNotMapped() {
        assertThat(this.resultBudgetTemplateRecord)
            .hasFieldOrPropertyWithValue("id", null);
    }

    @Test
    public void recordDateAttribute_isMapped() {
        LocalDate expectedRecordDate = LocalDate.of(9999, 12, this.sourceNewBudgetTemplateRecord.getDayOfMonth());
        assertThat(this.resultBudgetTemplateRecord)
            .hasFieldOrPropertyWithValue("recordDate", expectedRecordDate);
    }

    @Test
    public void accountIdAttribute_isMapped() {
        assertThat(this.resultBudgetTemplateRecord)
            .hasFieldOrPropertyWithValue("account.id", this.sourceNewBudgetTemplateRecord.getAccountId());
    }

    @Test
    public void categoryIdAttribute_isMapped() {
        assertThat(this.resultBudgetTemplateRecord)
            .hasFieldOrPropertyWithValue("category.id", this.sourceNewBudgetTemplateRecord.getCategoryId());
    }

    @Test
    public void descriptionAttribute_isMapped() {
        assertThat(this.resultBudgetTemplateRecord)
            .hasFieldOrPropertyWithValue("description", this.sourceNewBudgetTemplateRecord.getDescription());
    }

    @Test
    public void recordValueAttribute_isMapped() {
        assertThat(this.resultBudgetTemplateRecord)
            .hasFieldOrPropertyWithValue("recordValue", this.sourceNewBudgetTemplateRecord.getRecordValue());
    }

    @Test
    public void recordContainerAttribute_isNotMapped() {
        assertThat(this.resultBudgetTemplateRecord)
            .hasFieldOrPropertyWithValue("recordContainer", null);
    }

}
