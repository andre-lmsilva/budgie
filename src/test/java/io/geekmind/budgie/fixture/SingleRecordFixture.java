package io.geekmind.budgie.fixture;

import io.geekmind.budgie.model.entity.SingleRecord;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SingleRecordFixture {

    public static SingleRecord getIncomeRecord() {
        SingleRecord record = new SingleRecord();
        record.setRecordDate(LocalDate.now());
        record.setAccount(StandardAccountFixture.getMainAccount());
        record.setCategory(CategoryFixture.get());
        record.setDescription("Fake income record.");
        record.setRecordValue(BigDecimal.valueOf(3.1415D));
        return record;
    }

}
