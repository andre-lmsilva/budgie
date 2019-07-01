package io.geekmind.budgie.fixture;

import io.geekmind.budgie.model.dto.NewInstalment;

import java.math.BigDecimal;
import java.time.LocalDate;

public class NewInstalmentFixture {

    public static NewInstalment get() {
        return new NewInstalment(
                LocalDate.now(),
                -1,
                -99,
                "Fake Instalments",
                5,
                BigDecimal.TEN
        );
    }

}
