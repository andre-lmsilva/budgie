package io.geekmind.budgie.fixture;

import io.geekmind.budgie.model.entity.TransferRecord;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TransferRecordFixture {

    public static TransferRecord get() {
        TransferRecord record = new TransferRecord();
        record.setId(-99);
        record.setAccount(StandardAccountFixture.getMainAccount());
        record.setCategory(CategoryFixture.get());
        record.setRecordDate(LocalDate.now());
        record.setDescription("Fake Transfer");
        record.setRecordValue(BigDecimal.valueOf(-150D));
        record.setRecordContainer(TransferContainerFixture.get());
        return record;
    }

}
