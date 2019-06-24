package io.geekmind.budgie.model.mapper;

import io.geekmind.budgie.fixture.NewSingleRecordFixture;
import io.geekmind.budgie.model.dto.NewSingleRecord;
import io.geekmind.budgie.model.entity.SingleRecord;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;


public class NewSingleRecordToSingleRecordMapperTest {

    private NewSingleRecord newSingleRecordSource;
    private SingleRecord singleRecordResulting;

    @Before
    public void setUp() {
        this.newSingleRecordSource = NewSingleRecordFixture.get();
        this.singleRecordResulting = new NewSingleRecordToSingleRecordMapper().mapTo(this.newSingleRecordSource);
    }

    @Test
    public void idIsAlwaysNull() {
        assertThat(this.singleRecordResulting.getId()).isNull();
    }

    @Test
    public void recordDateIsMappedToSingleRecordRecordDateAttribute() {
        assertThat(this.singleRecordResulting)
            .hasFieldOrPropertyWithValue("recordDate", newSingleRecordSource.getRecordDate());
    }

    @Test
    public void accountIdIsMappedToSingleRecordAccountIdAttribute() {
        assertThat(this.singleRecordResulting)
            .hasFieldOrPropertyWithValue("account.id", newSingleRecordSource.getAccountId());
    }

    @Test
    public void categoryIdIsMappedToSingleRecordCategoryIdAttribute() {
        assertThat(this.singleRecordResulting)
            .hasFieldOrPropertyWithValue("category.id", newSingleRecordSource.getCategoryId());
    }

    @Test
    public void descriptionIsMappedToSingleRecordDescriptionAttribute() {
        assertThat(this.singleRecordResulting)
            .hasFieldOrPropertyWithValue("description", newSingleRecordSource.getDescription());
    }

    @Test
    public void recordValueIsMappedToSingleRecordRecordValueAttribute() {
        assertThat(this.singleRecordResulting)
            .hasFieldOrPropertyWithValue("recordValue", newSingleRecordSource.getRecordValue());
    }

}