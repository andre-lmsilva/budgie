package io.geekmind.budgie.repository;

import io.geekmind.budgie.model.dto.ExistingRecord;
import io.geekmind.budgie.model.dto.NewSingleRecord;
import io.geekmind.budgie.model.entity.Record;
import io.geekmind.budgie.model.entity.SingleRecord;
import io.geekmind.budgie.model.mapper.Mapper;
import io.geekmind.budgie.model.mapper.NewSingleRecordToSingleRecordMapper;
import io.geekmind.budgie.model.mapper.RecordToExistingRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class SingleRecordService {

    private final Mapper<NewSingleRecord, SingleRecord> newSingleRecordMapper;
    private final Mapper<Record, ExistingRecord> recordMapper;
    private final SingleRecordRepository singleRecordRepository;

    @Autowired
    public SingleRecordService(@Qualifier(NewSingleRecordToSingleRecordMapper.QUALIFIER)Mapper<NewSingleRecord, SingleRecord> newSingleRecordMapper,
                               @Qualifier(RecordToExistingRecordMapper.QUALIFIER)Mapper<Record, ExistingRecord> recordMapper,
                               SingleRecordRepository singleRecordRepository) {
        this.newSingleRecordMapper = newSingleRecordMapper;
        this.recordMapper = recordMapper;
        this.singleRecordRepository = singleRecordRepository;
    }

    public ExistingRecord create(NewSingleRecord newSingleRecord) {
        SingleRecord record = this.newSingleRecordMapper.mapTo(newSingleRecord);
        return this.recordMapper.mapTo(
            this.singleRecordRepository.save(record)
        );
    }



}
