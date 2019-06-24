package io.geekmind.budgie.repository;

import io.geekmind.budgie.fixture.ExistingRecordFixture;
import io.geekmind.budgie.model.dto.ExistingRecord;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class StandardBalanceServiceFiltersTest {

    private ExistingRecord nullValue = ExistingRecordFixture.getWithValue(null);
    private ExistingRecord positiveValue = ExistingRecordFixture.getWithValue(BigDecimal.TEN);
    private ExistingRecord zeroValue = ExistingRecordFixture.getWithValue(BigDecimal.ZERO);
    private ExistingRecord negativeValue = ExistingRecordFixture.getWithValue(BigDecimal.valueOf(-1D));

    @Test
    public void incomeRecordsFilter_WhenAppliedToAListOfExistingRecords_FilterOnlyTheOnesWithPositiveValuesDifferentOfZero() {
        List<ExistingRecord> result = Stream.of(nullValue, positiveValue, zeroValue, negativeValue)
            .filter(StandardBalanceService.INCOMES_RECORDS_FILTER)
            .collect(Collectors.toList());

        assertThat(result).containsOnly(positiveValue);
    }

    @Test
    public void expenseRecordsFilter_WhenAppliedToAListOfExistingRecords_FilterOnlyTheOnesWithNegativeValues() {
        List<ExistingRecord> result = Stream.of(nullValue, positiveValue, zeroValue, negativeValue)
            .filter(StandardBalanceService.EXPENSES_RECORDS_FILTER)
            .collect(Collectors.toList());

        assertThat(result).containsOnly(negativeValue);
    }

    @Test
    public void anyRecordsFilter_WhenAppliedToAListOfExistingRecords_FilterOnlyTheOnesWithNonNullRecordValue() {
        List<ExistingRecord> result = Stream.of(nullValue, positiveValue, zeroValue, negativeValue)
            .filter(StandardBalanceService.ANY_RECORDS_FILTER)
            .collect(Collectors.toList());

        assertThat(result).containsOnly(positiveValue, zeroValue, negativeValue);
    }

}
