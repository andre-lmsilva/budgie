package io.geekmind.budgie.csv;

import io.geekmind.budgie.model.dto.NewSingleRecord;
import io.geekmind.budgie.model.dto.standard_account.ExistingStandardAccount;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CSVParserService {

    public static final String[] FILE_HEADERS = { "record_date", "description", "bank_statement_id", "record_value" };

    public List<NewSingleRecord> parseCSVFile(MultipartFile csvFile, ExistingStandardAccount existingStandardAccount) throws IOException {
        StringReader csvContentReader = new StringReader(
            new String(csvFile.getBytes(), StandardCharsets.UTF_8)
        );

        CSVParser parser = CSVFormat.DEFAULT
            .withHeader(FILE_HEADERS)
            .withFirstRecordAsHeader()
            .parse(csvContentReader);

        return parser.getRecords()
            .stream()
            .map(record -> {
                NewSingleRecord singleRecord = new NewSingleRecord();
                singleRecord.setAccountId(existingStandardAccount.getId());
                singleRecord.setRecordDate(LocalDate.parse(record.get("record_date")));
                singleRecord.setDescription(record.get("description"));
                singleRecord.setBankStatementId(record.get("bank_statement_id"));
                singleRecord.setRecordValue(new BigDecimal(record.get("record_value")));
                return singleRecord;
            }).collect(Collectors.toList());
    }


}
