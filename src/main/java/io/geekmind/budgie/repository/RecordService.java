package io.geekmind.budgie.repository;

import io.geekmind.budgie.model.dto.ExistingRecord;
import io.geekmind.budgie.model.entity.Record;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecordService {

    private final RecordRepository recordRepository;
    private final MapperFacade mapper;

    @Autowired
    public RecordService(RecordRepository recordRepository,
                         MapperFacade mapper) {
        this.recordRepository = recordRepository;
        this.mapper = mapper;
    }

    public List<ExistingRecord> loadAll(Integer accountId, LocalDate startDate, LocalDate endDate) {
        return this.recordRepository.findByAccount_IdAndRecordDateBetweenOrderByRecordDate(accountId, startDate, endDate)
                .stream()
                .map(record -> this.mapper.map(record, ExistingRecord.class))
                .collect(Collectors.toList());
    }

    public Optional<ExistingRecord> remove(Integer id) {
        return this.recordRepository.findById(id)
            .map(record -> {
                this.recordRepository.delete(record);
                return this.mapper.map(record, ExistingRecord.class);
            });
    }

    public void save(Record entity) {
        this.recordRepository.save(entity);
    }
}
