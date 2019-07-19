package io.geekmind.budgie.fixture;

import io.geekmind.budgie.model.dto.NewImportedRecord;

import java.math.BigDecimal;
import java.time.LocalDate;

public class NewImportedRecordFixture {

    public static NewImportedRecord get() {
        NewImportedRecord record = new NewImportedRecord();
        record.setAccountId(-99);
        record.setImportedFileId(-100);
        record.setCategoryId(-101);
        record.setRecordDate(LocalDate.now());
        record.setDescription("Fake Description");
        record.setRecordValue(BigDecimal.valueOf(-3.14D));
        record.setMd5RecordHash("fakeHash");
        return record;
    }

}
