package io.geekmind.budgie.model.mapper;

import io.geekmind.budgie.model.dto.Balance;
import io.geekmind.budgie.model.dto.DependantAccountRecord;
import io.geekmind.budgie.model.dto.ExistingCategory;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component(BalanceToDependantAccountRecordMapper.QUALIFIER)
public class BalanceToDependantAccountRecordMapper implements Mapper<Balance, DependantAccountRecord> {

    public static final String QUALIFIER = "balanceToDependantAccountRecordMapper";
    private final ExistingCategory accountBalanceCategory;

    public BalanceToDependantAccountRecordMapper() {
        this.accountBalanceCategory = new ExistingCategory();
        this.accountBalanceCategory.setId(-1);
        this.accountBalanceCategory.setName("Account Balance");
    }

    @Override
    public DependantAccountRecord mapTo(Balance source) {
        DependantAccountRecord record = new DependantAccountRecord();
        record.setId(-1);
        record.setRecordDate(source.getBalanceDates().getPeriodBillingDate());
        record.setRecordValue(source.getSummary().getFinalBalance());
        record.setCategory(this.accountBalanceCategory);

        String formattedStartDate = source.getBalanceDates().getPeriodStartDate()
            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String formattedEndDate = source.getBalanceDates().getPeriodEndDate()
            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        record.setDescription(String.format(
            "Balance of %s account between %s and %s.",
            source.getAccount().getName(), formattedStartDate, formattedEndDate
        ));
        return record;
    }

    @Override
    public Balance mapFrom(DependantAccountRecord target) {
        throw new UnsupportedOperationException("There is not mapping in that direction.");
    }
}
