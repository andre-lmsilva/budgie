package io.geekmind.budgie.repository;

import io.geekmind.budgie.model.dto.ExistingRecord;
import io.geekmind.budgie.model.dto.NewSingleRecord;
import io.geekmind.budgie.model.entity.SingleRecord;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SingleRecordService {

    private final SingleRecordRepository singleRecordRepository;
    private final MapperFacade mapper;

    @Autowired
    public SingleRecordService(SingleRecordRepository singleRecordRepository,
                               MapperFacade mapper) {
        this.singleRecordRepository = singleRecordRepository;
        this.mapper = mapper;
    }

    public ExistingRecord create(NewSingleRecord newSingleRecord) {
        SingleRecord record = this.mapper.map(newSingleRecord, SingleRecord.class);
        return this.mapper.map(
            this.singleRecordRepository.save(record),
            ExistingRecord.class
        );
    }
}
