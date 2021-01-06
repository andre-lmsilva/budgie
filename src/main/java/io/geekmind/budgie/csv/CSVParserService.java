package io.geekmind.budgie.csv;

import io.geekmind.budgie.model.dto.NewSingleRecord;
import io.geekmind.budgie.model.dto.account_parameter.ExistingAccountParameter;
import io.geekmind.budgie.model.dto.standard_account.ExistingStandardAccount;
import io.geekmind.budgie.model.entity.AccountParameterKey;
import io.geekmind.budgie.repository.AccountParameterService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CSVParserService {

    public static final String[] FILE_HEADERS = {
        "posted_account",
        "record_date",
        "description",
        "debit",
        "credit",
        "balance",
        "transaction_type"
    };

    private final HashService hashService;
    private final AccountParameterService accountParameterService;
    private DateTimeFormatter dateTimeFormatter;

    public CSVParserService(HashService hashService,
                            AccountParameterService accountParameterService) {
        this.hashService = hashService;
        this.accountParameterService = accountParameterService;
        dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yy");
    }

    public List<NewSingleRecord> parseCSVFile(MultipartFile csvFile, ExistingStandardAccount existingStandardAccount) throws IOException {
        StringReader csvContentReader = new StringReader(
            new String(csvFile.getBytes(), StandardCharsets.UTF_8)
        );

        CSVParser parser = CSVFormat.DEFAULT
            .withSkipHeaderRecord()
            .withHeader(FILE_HEADERS)
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
                singleRecord.setRecordDate(LocalDate.parse(record.get("record_date"), this.dateTimeFormatter));
                singleRecord.setDescription(record.get("description"));
                singleRecord.setBankStatementId(record.get("description"));

                if (record.get("transaction_type").toUpperCase().equals("DEBIT")) {
                    singleRecord.setRecordValue(
                        new BigDecimal(record.get("debit")).negate()
                    );
                } else if (record.get("transaction_type").toUpperCase().equals("CREDIT")) {
                    singleRecord.setRecordValue(
                        new BigDecimal(record.get("credit"))
                    );
                } else {
                    singleRecord.setRecordValue(BigDecimal.ZERO);
                }

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
