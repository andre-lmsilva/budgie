package io.geekmind.budgie.model.dto.report;

import io.geekmind.budgie.model.dto.ExistingRecord;
import io.geekmind.budgie.model.dto.category.ExistingCategory;
import io.geekmind.budgie.model.dto.standard_account.ExistingStandardAccount;
import lombok.Value;

import java.math.BigDecimal;
import java.util.List;

@Value
public class TaxRefundableCategorySummary {

    private ExistingStandardAccount account;

    private ExistingCategory category;

    private BigDecimal balance;

    private List<ExistingRecord> records;

}
