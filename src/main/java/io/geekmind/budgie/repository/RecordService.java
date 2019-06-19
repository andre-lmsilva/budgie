package io.geekmind.budgie.repository;

import io.geekmind.budgie.model.dto.ExistingRecord;
import io.geekmind.budgie.model.entity.Record;
import io.geekmind.budgie.model.mapper.Mapper;
import io.geekmind.budgie.model.mapper.RecordToExistingRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecordService {

    private final RecordRepository recordRepository;
    private final Mapper<Record, ExistingRecord> recordMapper;

    @Autowired
    public RecordService(RecordRepository recordRepository,
                         @Qualifier(RecordToExistingRecordMapper.QUALIFIER)Mapper<Record, ExistingRecord> recordMapper) {
        this.recordRepository = recordRepository;
        this.recordMapper = recordMapper;
    }

    public List<ExistingRecord> loadAll(Integer accountId, LocalDate startDate, LocalDate endDate) {
        return this.recordRepository.findByAccount_IdAndRecordDateBetweenOrderByRecordDate(accountId, startDate, endDate)
                .stream()
                .map(this.recordMapper::mapTo)
                .collect(Collectors.toList());
    }

    public Optional<ExistingRecord> remove(Integer id) {
        return this.recordRepository.findById(id)
            .map(record -> {
                this.recordRepository.delete(record);
                return this.recordMapper.mapTo(record);
            });
    }



}
