package io.geekmind.budgie.fixture;

import io.geekmind.budgie.model.dto.NewSingleRecord;

import java.math.BigDecimal;
import java.time.LocalDate;

public class NewSingleRecordFixture {

    public static NewSingleRecord get() {
        return new NewSingleRecord(
            LocalDate.now(),
            -1,
            -99,
            "Fake Single Record",
            BigDecimal.valueOf(3.1415D)
        );
    }
}