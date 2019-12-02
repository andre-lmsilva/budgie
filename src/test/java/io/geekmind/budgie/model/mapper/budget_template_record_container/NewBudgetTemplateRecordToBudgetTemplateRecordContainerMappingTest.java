package io.geekmind.budgie.model.mapper.budget_template_record_container;

import io.geekmind.budgie.fixture.NewBudgetTemplateRecordFixture;
import io.geekmind.budgie.model.dto.budget_template_record.NewBudgetTemplateRecord;
import io.geekmind.budgie.model.entity.BudgetTemplateRecordContainer;
import io.geekmind.budgie.model.mapper.BudgetTemplateRecordContainerMappingSettings;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class NewBudgetTemplateRecordToBudgetTemplateRecordContainerMappingTest {

    private NewBudgetTemplateRecord sourceNewBudgetTemplateRecord;
    private BudgetTemplateRecordContainer resultBudgetTemplateRecordContainer;

    @Before
    public void setUp() {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        new BudgetTemplateRecordContainerMappingSettings().configure(mapperFactory);
        this.sourceNewBudgetTemplateRecord = NewBudgetTemplateRecordFixture.salary();
        this.resultBudgetTemplateRecordContainer = mapperFactory.getMapperFacade()
            .map(this.sourceNewBudgetTemplateRecord, BudgetTemplateRecordContainer.class);
    }

    @Test
    public void idAttribute_isNotMapped() {
        assertThat(this.resultBudgetTemplateRecordContainer)
                .hasFieldOrPropertyWithValue("id", null);
    }

    @Test
    public void nameAttribute_isMapped() {
        String expectedName = String.format(
            "Account id: %s / Category id: %s / Day of Month: %s / Description: %s",
            this.sourceNewBudgetTemplateRecord.getAccountId(),
            this.sourceNewBudgetTemplateRecord.getCategoryId(),
            this.sourceNewBudgetTemplateRecord.getDayOfMonth(),
            this.sourceNewBudgetTemplateRecord.getDescription()
        );
        assertThat(this.resultBudgetTemplateRecordContainer)
            .hasFieldOrPropertyWithValue("name", expectedName);
    }

    @Test
    public void recordsAttribute_isInitialized() {
        assertThat(this.resultBudgetTemplateRecordContainer)
            .hasFieldOrPropertyWithValue("records", Collections.emptyList());
    }

}
