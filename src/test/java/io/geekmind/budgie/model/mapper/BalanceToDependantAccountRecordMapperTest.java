package io.geekmind.budgie.model.mapper;

import io.geekmind.budgie.fixture.BalanceFixture;
import io.geekmind.budgie.model.dto.Balance;
import io.geekmind.budgie.model.dto.DependantAccountRecord;
import org.junit.Before;
import org.junit.Test;

import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class BalanceToDependantAccountRecordMapperTest {

    private Balance balanceSource;
    private DependantAccountRecord resultingDependantAccountRecord;

    @Before
    public void setUp() {
        this.balanceSource = BalanceFixture.getCurrentPeriodBalance();
        this.resultingDependantAccountRecord = new BalanceToDependantAccountRecordMapper().mapTo(this.balanceSource);
    }

    @Test
    public void idAttributeIsAlwaysEqualsToMinusOne() {
        assertThat(this.resultingDependantAccountRecord).hasFieldOrPropertyWithValue("id", -1);
    }

    @Test
    public void billingDateIsMappedToDependantAccountRecordRecordDateAttribute() {
        assertThat(this.resultingDependantAccountRecord)
            .hasFieldOrPropertyWithValue("recordDate", this.balanceSource.getBalanceDates().getPeriodBillingDate());
    }

    @Test
    public void customDescriptionIsCreatedWithAccountNameAndPeriodStartAndEndDates() {
        String expectedDescription = String.format("Balance of %s account between %s and %s.",
            this.balanceSource.getAccount().getName(),
            this.balanceSource.getBalanceDates().getPeriodStartDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
            this.balanceSource.getBalanceDates().getPeriodEndDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        );

        assertThat(this.resultingDependantAccountRecord)
            .hasFieldOrPropertyWithValue("description", expectedDescription);
    }

    @Test
    public void finalBalanceIsMappedToDependantAccountRecordRecordValueAttribute() {
        assertThat(this.resultingDependantAccountRecord)
            .hasFieldOrPropertyWithValue("recordValue", this.balanceSource.getSummary().getFinalBalance());
    }

    @Test
    public void accountIsMappedToDependantAccountCategoryAttribute() {
        assertThat(this.resultingDependantAccountRecord)
            .hasFieldOrPropertyWithValue("account", this.balanceSource.getAccount());
    }

    @Test
    public void categoryIsFilledWithCustomCategory() {
        assertThat(this.resultingDependantAccountRecord.getCategory())
            .hasFieldOrPropertyWithValue("id", -1)
            .hasFieldOrPropertyWithValue("name", "Account Balance");
    }
}