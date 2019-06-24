package io.geekmind.budgie.model.mapper;

import io.geekmind.budgie.model.dto.NewSingleRecord;
import io.geekmind.budgie.model.entity.Account;
import io.geekmind.budgie.model.entity.Category;
import io.geekmind.budgie.model.entity.SingleRecord;
import org.springframework.stereotype.Component;

/**
 * Maps attributes from {@link NewSingleRecord} to attributes of a new {@link SingleRecord} instance.
 *
 * @author Andre Silva
 */
@Component(NewSingleRecordToSingleRecordMapper.QUALIFIER)
public class NewSingleRecordToSingleRecordMapper implements Mapper<NewSingleRecord, SingleRecord> {

    public static final String QUALIFIER = "newSingleRecordToSingleRecordMapper";

    @Override
    public SingleRecord mapTo(NewSingleRecord source) {
        SingleRecord singleRecord = new SingleRecord();
        singleRecord.setRecordDate(source.getRecordDate());
        singleRecord.setDescription(source.getDescription());
        singleRecord.setRecordValue(source.getRecordValue());

        Account account = new Account();
        account.setId(source.getAccountId());
        singleRecord.setAccount(account);

        Category category = new Category();
        category.setId(source.getCategoryId());
        singleRecord.setCategory(category);

        return singleRecord;
    }

    @Override
    public NewSingleRecord mapFrom(SingleRecord target) {
        throw new UnsupportedOperationException("There is no mapping in that direction.");
    }
}
