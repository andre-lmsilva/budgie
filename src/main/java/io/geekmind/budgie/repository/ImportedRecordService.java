package io.geekmind.budgie.repository;

import io.geekmind.budgie.model.dto.ExistingImportedRecord;
import io.geekmind.budgie.model.dto.NewImportedRecord;
import io.geekmind.budgie.model.entity.ImportedRecord;
import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ImportedRecordService {

    private final ImportedRecordRepository importedRecordRepository;
    private final MapperFacade mapper;

    public ImportedRecordService(ImportedRecordRepository importedRecordRepository,
                                 MapperFacade mapper) {
        this.importedRecordRepository = importedRecordRepository;
        this.mapper = mapper;
    }

    public ExistingImportedRecord save(NewImportedRecord newImportedRecord) {
        ImportedRecord importedRecord = this.mapper.map(newImportedRecord, ImportedRecord.class);
        return this.mapper.map(
            this.importedRecordRepository.save(importedRecord),
            ExistingImportedRecord.class
        );
    }

    public Optional<ImportedRecord> findBySourceMd5Hash(String hash) {
        return this.importedRecordRepository.findAll()
            .stream()
            .filter(importedRecord -> importedRecord.getSourceMd5Hash().equals(hash))
            .findFirst();
    }
}
