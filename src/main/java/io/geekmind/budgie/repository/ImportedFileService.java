package io.geekmind.budgie.repository;

import io.geekmind.budgie.csv.CSVParser;
import io.geekmind.budgie.model.dto.*;
import io.geekmind.budgie.model.entity.ImportedFile;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ImportedFileService {

    private final ImportedFileRepository importedFileRepository;
    private final AccountService accountService;
    private final ImportedRecordService importedRecordService;
    private final CSVParser csvParser;
    private final MapperFacade mapper;

    @Autowired
    public ImportedFileService(ImportedFileRepository importedFileRepository,
                               AccountService accountService,
                               ImportedRecordService importedRecordService,
                               CSVParser csvParser,
                               MapperFacade mapper) {
        this.importedFileRepository = importedFileRepository;
        this.accountService = accountService;
        this.importedRecordService = importedRecordService;
        this.csvParser = csvParser;
        this.mapper = mapper;
    }

    public NewImportedFile create(MultipartFile uploadedFile) {
        ImportedFile importedFile = this.mapper.map(uploadedFile, ImportedFile.class);
        byte[] fileContent;
        try {
            fileContent = uploadedFile.getBytes();
        } catch (IOException ex) {
            throw new RuntimeException(
                String.format("It was not possible to access the uploaded file content due the following exception: %s", ex.getMessage())
            );
        }

        List<NewImportedRecord> importedRecords = this.csvParser.parseImportedFile(fileContent)
            .stream()
            .filter(record -> BigDecimal.ZERO.compareTo(record.getRecordValue()) != 0)
            .filter(record -> !this.importedRecordService.findBySourceMd5Hash(record.getMd5RecordHash()).isPresent())
            .collect(Collectors.toList());

        LocalDate startDate = importedRecords
            .stream()
            .min(Comparator.comparing((NewImportedRecord::getRecordDate)))
            .map(NewImportedRecord::getRecordDate)
            .orElseThrow(NoSuchElementException::new);

        LocalDate endDate = importedRecords
            .stream()
            .max(Comparator.comparing(NewImportedRecord::getRecordDate))
            .map(NewImportedRecord::getRecordDate)
            .orElseThrow(NoSuchElementException::new);

        importedFile.setStartDate(startDate);
        importedFile.setEndDate(endDate);
        NewImportedFile newImportedFile = this.mapper.map(
            this.importedFileRepository.save(importedFile),
            NewImportedFile.class
        );

        importedRecords.forEach(newImportedFile::addRecord);
        return newImportedFile;
    }

    @Transactional
    public List<ExistingImportedRecord> saveRecords(NewImportedFile importedFile) {
        ExistingAccount mainAccount = this.accountService.getMainAccount()
            .orElseThrow(() -> new IllegalStateException("There is no main account defined."));
        List<ExistingImportedRecord> result = new ArrayList<>();
        for(NewImportedRecord record: importedFile.getNewImportedRecords()) {
            record.setAccountId(mainAccount.getId());
            result.add(this.importedRecordService.save(record));
        }
        return result;
    }

    public List<ExistingImportedFile> loadAll() {
        return this.importedFileRepository.findAll(Sort.by(Sort.Direction.DESC, "importedAt"))
            .stream()
            .map(file -> this.mapper.map(file, ExistingImportedFile.class))
            .collect(Collectors.toList());
    }

    public Optional<ExistingImportedFile> remove(Integer id) {
        return this.importedFileRepository.findById(id)
            .map(importedFile -> {
                ExistingImportedFile existingImportedFile = this.mapper.map(importedFile, ExistingImportedFile.class);
                this.importedFileRepository.delete(importedFile);
                return existingImportedFile;
            });
    }

}
