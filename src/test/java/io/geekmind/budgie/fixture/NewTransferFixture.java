package io.geekmind.budgie.fixture;

import io.geekmind.budgie.model.dto.NewTransfer;

import java.math.BigDecimal;
import java.time.LocalDate;

public class NewTransferFixture {

    public static NewTransfer get() {
        NewTransfer transfer = new NewTransfer();
        transfer.setCategoryId(-1);
        transfer.setDescription("Fake Transfer");
        transfer.setDestinationAccountId(-2);
        transfer.setSourceAccountId(-3);
        transfer.setTransferDate(LocalDate.now());
        transfer.setTransferValue(BigDecimal.valueOf(-4D));
        return transfer;
    }
}
