package io.geekmind.budgie.repository;

import io.geekmind.budgie.model.dto.ExistingRecord;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class StandardBalanceServicePeriodIntervalFilterTest {

    private ExistingRecord beginningOfPeriodRecord;
    private ExistingRecord middleOfPeriodRecord;
    private ExistingRecord endOfPeriodRecord;
    private ExistingRecord afterPeriodRecord;
    private ExistingRecord beforePeriodRecord;
    private List<ExistingRecord> result;

    @Before
    public void setUp() {
        this.beginningOfPeriodRecord = new ExistingRecord();
        this.beginningOfPeriodRecord.setRecordDate(LocalDate.now());

        this.middleOfPeriodRecord = new ExistingRecord();
        this.middleOfPeriodRecord.setRecordDate(LocalDate.now().plusDays(1L));

        this.endOfPeriodRecord = new ExistingRecord();
        this.endOfPeriodRecord.setRecordDate(LocalDate.now().plusDays(2L));

        this.afterPeriodRecord = new ExistingRecord();
        this.afterPeriodRecord.setRecordDate(LocalDate.now().plusDays(3L));

        this.beforePeriodRecord = new ExistingRecord();
        this.beforePeriodRecord.setRecordDate(LocalDate.now().minusDays(1L));

        this.result = Stream.of(
            this.beforePeriodRecord,
            this.beginningOfPeriodRecord,
            this.middleOfPeriodRecord,
            this.endOfPeriodRecord,
            this.afterPeriodRecord
        ).filter(
            new StandardBalanceService(null, null, null)
                .periodIntervalFilter(LocalDate.now(), LocalDate.now().plusDays(2L))
        ).collect(Collectors.toList());
    }

    @Test
    public void containsOnlyRecordsWhichRecordDateIsWithinTheDefinedPeriod() {
        assertThat(this.result)
            .containsOnly(this.beginningOfPeriodRecord, this.middleOfPeriodRecord, this.endOfPeriodRecord);
    }
}
