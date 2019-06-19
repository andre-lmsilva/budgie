package io.geekmind.budgie.model.mapper;

import io.geekmind.budgie.model.dto.ExistingAccount;
import io.geekmind.budgie.model.dto.ExistingCategory;
import io.geekmind.budgie.model.dto.ExistingRecord;
import io.geekmind.budgie.model.entity.Account;
import io.geekmind.budgie.model.entity.Category;
import io.geekmind.budgie.model.entity.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component(RecordToExistingRecordMapper.QUALIFIER)
public class RecordToExistingRecordMapper implements Mapper<Record, ExistingRecord> {

    public static final String QUALIFIER = "recordToExistingRecordMapper";
    private final Mapper<Account, ExistingAccount> accountMapper;
    private final Mapper<Category, ExistingCategory> categoryMapper;

    @Autowired
    public RecordToExistingRecordMapper(@Qualifier(AccountToExistingAccountMapper.QUALIFIER)Mapper<Account, ExistingAccount> accountMapper,
                                        @Qualifier(CategoryToExistingCategoryMapper.QUALIFIER)Mapper<Category, ExistingCategory> categoryMapper) {
        this.accountMapper = accountMapper;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public ExistingRecord mapTo(Record source) {
        ExistingRecord record = new ExistingRecord();
        record.setId(source.getId());
        record.setDescription(source.getDescription());
        record.setRecordDate(source.getRecordDate());
        record.setRecordValue(source.getRecordValue());

        record.setAccount(
            this.accountMapper.mapTo(source.getAccount())
        );

        record.setCategory(
            this.categoryMapper.mapTo(source.getCategory())
        );

        return record;
    }

    @Override
    public Record mapFrom(ExistingRecord target) {
        throw new UnsupportedOperationException("There is no mapping in that direction.");
    }
}
