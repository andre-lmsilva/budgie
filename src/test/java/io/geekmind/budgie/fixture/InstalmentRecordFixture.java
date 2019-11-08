package io.geekmind.budgie.fixture;

import io.geekmind.budgie.model.entity.InstalmentRecord;

import java.math.BigDecimal;
import java.time.LocalDate;

public class InstalmentRecordFixture {

    public static InstalmentRecord getWithoutInstalmentContainer() {
        InstalmentRecord record = new InstalmentRecord();
        record.setId(-1);
        record.setRecordDate(LocalDate.now());
        record.setAccount(StandardAccountFixture.getMainAccount());
        record.setCategory(CategoryFixture.get());
        record.setDescription("Fake Instalment Record");
        record.setRecordValue(BigDecimal.valueOf(-3.1415D));
        return record;
    }

}
