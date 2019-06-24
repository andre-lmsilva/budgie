package io.geekmind.budgie.model.mapper;

import io.geekmind.budgie.fixture.ExistingAccountFixture;
import io.geekmind.budgie.fixture.ExistingCategoryFixture;
import io.geekmind.budgie.fixture.SingleRecordFixture;
import io.geekmind.budgie.model.dto.ExistingAccount;
import io.geekmind.budgie.model.dto.ExistingCategory;
import io.geekmind.budgie.model.dto.ExistingRecord;
import io.geekmind.budgie.model.entity.Account;
import io.geekmind.budgie.model.entity.Category;
import io.geekmind.budgie.model.entity.SingleRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class RecordToExistingRecordMapperTest {

    @Mock
    private Mapper<Account, ExistingAccount> accountMapper;

    @Mock
    private Mapper<Category, ExistingCategory> categoryMapper;

    private RecordToExistingRecordMapper mapper;

    private SingleRecord recordSource;
    private ExistingAccount resultingAccount;
    private ExistingCategory resultingCategory;
    private ExistingRecord resultingExistingRecord;

    @Before
    public void setUp() {
        this.mapper = new RecordToExistingRecordMapper(this.accountMapper, this.categoryMapper);

        this.recordSource = SingleRecordFixture.getIncomeRecord();
        this.resultingAccount = ExistingAccountFixture.getMainAccount();
        this.resultingCategory = ExistingCategoryFixture.get();

        doReturn(this.resultingAccount).when(this.accountMapper).mapTo(eq(recordSource.getAccount()));
        doReturn(this.resultingCategory).when(this.categoryMapper).mapTo(eq(recordSource.getCategory()));

        this.resultingExistingRecord = this.mapper.mapTo(this.recordSource);
    }

    @Test
    public void accountIsMappedToExistingRecordAccountAttribute() {
        assertThat(this.resultingExistingRecord).hasFieldOrPropertyWithValue("account", this.resultingAccount);
        verify(this.accountMapper).mapTo(eq(this.recordSource.getAccount()));
    }

    @Test
    public void categoryIsMappedToExistingRecordCategoryAttribute() {
        assertThat(this.resultingExistingRecord).hasFieldOrPropertyWithValue("category", this.resultingCategory);
        verify(this.categoryMapper).mapTo(eq(this.recordSource.getCategory()));
    }

    @Test
    public void idIsMappedToExistingRecordIdAttribute() {
        assertThat(this.resultingExistingRecord)
            .hasFieldOrPropertyWithValue("id", this.recordSource.getId());
    }

    @Test
    public void recordDateIsMappedToExistingRecordRecordDateAttribute() {
        assertThat(this.resultingExistingRecord)
            .hasFieldOrPropertyWithValue("recordDate", this.recordSource.getRecordDate());
    }

    @Test
    public void descriptionIsMappedToExistingRecordDescriptionAttribute() {
        assertThat(this.resultingExistingRecord)
            .hasFieldOrPropertyWithValue("description", this.recordSource.getDescription());
    }

    @Test
    public void recordValueIsMappedToExistingRecordRecordValueAttribute() {
        assertThat(this.resultingExistingRecord)
            .hasFieldOrPropertyWithValue("recordValue", this.recordSource.getRecordValue());
    }

}