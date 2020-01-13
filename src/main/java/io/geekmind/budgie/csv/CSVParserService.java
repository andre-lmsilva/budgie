package io.geekmind.budgie.csv;

import io.geekmind.budgie.model.dto.NewSingleRecord;
import io.geekmind.budgie.model.dto.account_parameter.ExistingAccountParameter;
import io.geekmind.budgie.model.dto.standard_account.ExistingStandardAccount;
import io.geekmind.budgie.model.entity.AccountParameter;
import io.geekmind.budgie.model.entity.AccountParameterKey;
import io.geekmind.budgie.repository.AccountParameterService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CSVParserService {

    public static final String[] FILE_HEADERS = { "record_date", "description", "bank_statement_id", "record_value" };
    private final HashService hashService;
    private final AccountParameterService accountParameterService;

    public CSVParserService(HashService hashService,
                            AccountParameterService accountParameterService) {
        this.hashService = hashService;
        this.accountParameterService = accountParameterService;
    }

    public List<NewSingleRecord> parseCSVFile(MultipartFile csvFile, ExistingStandardAccount existingStandardAccount) throws IOException {
        StringReader csvContentReader = new StringReader(
            new String(csvFile.getBytes(), StandardCharsets.UTF_8)
        );

        CSVParser parser = CSVFormat.DEFAULT
            .withHeader(FILE_HEADERS)
            .withFirstRecordAsHeader()
            .parse(csvContentReader);

        String mostRecentlyImportedCSVRecordHash = this.accountParameterService
            .loadByAccountAndKey(existingStandardAccount, AccountParameterKey.MOST_RECENTLY_CSV_RECORD_IMPORTED)
            .map(ExistingAccountParameter::getValue)
            .orElse("");

        List<NewSingleRecord> importedRecords =  parser.getRecords()
            .stream()
            .map(record -> {
                NewSingleRecord singleRecord = new NewSingleRecord();
                singleRecord.setAccountId(existingStandardAccount.getId());
                singleRecord.setRecordDate(LocalDate.parse(record.get("record_date")));
                singleRecord.setDescription(record.get("description"));
                singleRecord.setBankStatementId(record.get("bank_statement_id"));
                singleRecord.setRecordValue(new BigDecimal(record.get("record_value")));
                singleRecord.setSourceRecordHash(
                    this.hashService.calculateMD5(record.toMap().toString())
                );
                return singleRecord;
            }).collect(Collectors.toList());

        List<NewSingleRecord> newRecords = new ArrayList<>();
        for(NewSingleRecord record: importedRecords) {
            if (!mostRecentlyImportedCSVRecordHash.equals(record.getSourceRecordHash())) {
                newRecords.add(record);
            } else {
                break;
            }
        }
        return newRecords;
    }


}
