package io.geekmind.budgie.fixture;

import io.geekmind.budgie.model.dto.ExistingRecord;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExistingRecordFixture {

    public static ExistingRecord getWithValue(BigDecimal value) {
        ExistingRecord existingRecord = new ExistingRecord();
        existingRecord.setRecordDate(LocalDate.now());
        existingRecord.setAccount(ExistingAccountFixture.getMainAccount());
        existingRecord.setCategory(ExistingCategoryFixture.get());
        existingRecord.setDescription("Fake record.");
        existingRecord.setId(-1);
        existingRecord.setRecordValue(value);
        return existingRecord;
    }

}
