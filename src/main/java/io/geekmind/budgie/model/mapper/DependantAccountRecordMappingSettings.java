package io.geekmind.budgie.model.mapper;

import io.geekmind.budgie.model.dto.balance.Balance;
import io.geekmind.budgie.model.dto.DependantAccountRecord;
import io.geekmind.budgie.model.dto.category.ExistingCategory;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import net.rakugakibox.spring.boot.orika.OrikaMapperFactoryConfigurer;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

@Component
public class DependantAccountRecordMappingSettings implements OrikaMapperFactoryConfigurer {

    private final ExistingCategory accountBalanceCategory;

    public DependantAccountRecordMappingSettings() {
        this.accountBalanceCategory = new ExistingCategory();
        this.accountBalanceCategory.setId(-1);
        this.accountBalanceCategory.setName("Account Balance");
        this.accountBalanceCategory.setDescription("Dependant account balance result.");
        this.accountBalanceCategory.setMaxExpenses(BigDecimal.ZERO);
    }

    @Override
    public void configure(MapperFactory orikaMapperFactory) {
        orikaMapperFactory.classMap(Balance.class, DependantAccountRecord.class)
                .customize(new CustomMapper<Balance, DependantAccountRecord>() {
                    @Override
                    public void mapAtoB(Balance balance, DependantAccountRecord dependantAccountRecord, MappingContext context) {
                        super.mapAtoB(balance, dependantAccountRecord, context);
                        dependantAccountRecord.setRecordType(DependantAccountRecord.class.getSimpleName());
                        dependantAccountRecord.setId(-1);
                        dependantAccountRecord.setRecordDate(balance.getBalanceDates().getPeriodBillingDate());
                        dependantAccountRecord.setPeriodStartDate(balance.getBalanceDates().getPeriodStartDate());
                        dependantAccountRecord.setRecordValue(balance.getSummary().getFinalBalance());
                        dependantAccountRecord.setCategory(accountBalanceCategory);
                        dependantAccountRecord.setAccount(balance.getAccount());
                        String formattedStartDate = balance.getBalanceDates().getPeriodStartDate()
                            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                        String formattedEndDate = balance.getBalanceDates().getPeriodEndDate()
                            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                        dependantAccountRecord.setDescription(
                            String.format(
                                "Balance of %s account between %s and %s.",
                                balance.getAccount().getName(),
                                formattedStartDate,
                                formattedEndDate
                            )
                        );
                    }
                }).register();

    }
}
