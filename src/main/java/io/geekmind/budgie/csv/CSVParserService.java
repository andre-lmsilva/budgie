package io.geekmind.budgie.csv;

import io.geekmind.budgie.configuration.CSVParserConfiguration;
import io.geekmind.budgie.csv.wrapper.CSVFormatWrapper;
import io.geekmind.budgie.csv.wrapper.CSVParserWrapper;
import io.geekmind.budgie.csv.wrapper.CSVRecordWrapper;
import io.geekmind.budgie.model.dto.NewSingleRecord;
import io.geekmind.budgie.model.dto.account.ExistingAccount;
import io.geekmind.budgie.model.dto.account_parameter.ExistingAccountParameter;
import io.geekmind.budgie.model.dto.standard_account.ExistingStandardAccount;
import io.geekmind.budgie.model.entity.AccountParameterKey;
import io.geekmind.budgie.repository.AccountParameterService;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Parses a CSV file, extracting its records (lines) and mapping into {@link NewSingleRecord} instance. Only supports
 * the AIB bank account format.
 *
 * @author Andre Silva
 */
@Service
public class CSVParserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CSVParserService.class);

    private final HashService hashService;
    private final AccountParameterService accountParameterService;
    private final CSVFormatWrapper aibBankAccountCSVFormat;
    private DateTimeFormatter dateTimeFormatter;

    /**
     * Default constructor.
     * @param hashService               Used to calculate the hash of each CSV record.
     * @param accountParameterService   Used to retrieve the last imported CSV record hash.
     * @param aibBankAccountCSVFormat   Used to parse the received CSV file content.
     */
    public CSVParserService(HashService hashService,
                            AccountParameterService accountParameterService,
                            @Qualifier(CSVParserConfiguration.AIB_BANK_ACCOUNT_CSV_FORMAT) CSVFormatWrapper aibBankAccountCSVFormat) {
        this.hashService = hashService;
        this.accountParameterService = accountParameterService;
        this.aibBankAccountCSVFormat = aibBankAccountCSVFormat;
    }

    @PostConstruct
    public void initialize() {
        this.dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yy");
    }

    /**
     * Parses an uploaded CSV file, extracting its records, mapping into {@link NewSingleRecord} instances and filtering
     * to exclude the already imported records.
     *
     * @param csvFile                   Received CSV file.
     * @param existingStandardAccount   Account which records will be assigned.
     * @return  A list of CSV records mapped into {@link NewSingleRecord} instance and filtered to contain only
     * records not imported yet.
     */
    public List<NewSingleRecord> parseCSVFile(MultipartFile csvFile, ExistingStandardAccount existingStandardAccount)  {
        LOGGER.info("Parsing received CSV file for account: {}", existingStandardAccount.toString());
        CSVParserWrapper parser = this.getCSVParser(csvFile);
        List<NewSingleRecord> newRecords;
        try {
            newRecords = this.mapFrom(parser.getRecords(), existingStandardAccount.getId());
            LOGGER.info("{} records processed from the received file.", newRecords.size());
        } catch (Exception ex) {
            throw new CSVParsingException(
                String.format("It was not possible to obtain the CSV records due the following exception: " +
                    "%s", ex.getMessage()), ex
            );
        }

        String lastImportedRecordHash = this.getLastImportedRecordHash(existingStandardAccount);
        return this.filterNonImportedRecords(newRecords, lastImportedRecordHash);
    }

    /**
     * Parses the receives CSV file into a list of {@link CSVRecord} to be mapped into {@link NewSingleRecord}
     * instances later on.
     * @param content   Received CSV file.
     * @return {@link CSVParserWrapper} that can be used to access the parsed list of {@link CSVRecord}.
     */
    protected CSVParserWrapper getCSVParser(MultipartFile content) {
        try {
            String csvContent = this.readCSVContent(content);
            LOGGER.debug("Parsing record from CSV content:\n{}", csvContent);

            StringReader csvContentReader = new StringReader(csvContent);
            return this.aibBankAccountCSVFormat.parse(csvContentReader);
        } catch (IOException ex) {
            throw new CSVParsingException(
                String.format("It was not possible to parse the received CSV file due the following exception: %s",
                    ex.getMessage()), ex
            );
        }
    }

    /**
     * Extracts the uploaded CSV content to a String.
     *
     * @param content Uploaded CSV content.
     * @return Content as extract from the uploaded file, converted into a UTF-8 encoded String.
     */
    protected String readCSVContent(MultipartFile content) {
        try {
            return new String(content.getBytes(), StandardCharsets.UTF_8);
        } catch (IOException ex) {
            throw new CSVParsingException(
                String.format("It was not possible to read the content of the uploaded file due the following " +
                        "exception: %s", ex.getMessage()), ex
            );
        }
    }

    /**
     * Returns the has of the last processed CSV record for the account which the CSV is being imported.
     * @param existingAccount   Account which CSV is being imported.
     * @return  The hash of the last processed CSV record of the received account or blank string if no record was
     * processed yet.
     */
    protected String getLastImportedRecordHash(ExistingAccount existingAccount) {
        return this.accountParameterService
            .loadByAccountAndKey(existingAccount, AccountParameterKey.MOST_RECENTLY_CSV_RECORD_IMPORTED)
            .map(ExistingAccountParameter::getValue)
            .orElse("");
    }

    /**
     * Maps the list of received CSV records into instances of {@link NewSingleRecord}. This method delegates to
     * {@link #mapFrom(CSVRecordWrapper, Integer)} to map each record. Records with value equals to zero are ignored.
     *
     * @param records       List of records to be mapped.
     * @param accountId     Id of the destination account.
     *
     * @return List of mapped records, ignoring the ones with value equals to zero.
     */
    protected List<NewSingleRecord> mapFrom(List<CSVRecordWrapper> records, Integer accountId) {
        return Collections.unmodifiableList(
            records.stream()
                .filter((CSVRecordWrapper csvRecord) -> {
                    String transactionType = csvRecord.get("transaction_type").toLowerCase().trim();
                    BigDecimal transactionValue = new BigDecimal(csvRecord.get(transactionType));
                    return BigDecimal.ZERO.compareTo(transactionValue) != 0;
                })
                .map((CSVRecordWrapper csvRecord) -> {
                    LOGGER.debug("Parsing CSV record from line [{}]: {}", csvRecord.getRecordNumber(), csvRecord.toMap().toString());
                    return this.mapFrom(csvRecord, accountId);
                }).collect(Collectors.toList())
        );
    }

    /**
     * Maps a single CSV record into a new instance of {@link NewSingleRecord}.
     *
     * @param csvRecord CSV record to be mapped.
     * @param accountId Id of destination account.
     *
     * @return New instance of {@link NewSingleRecord} filled in with data from the received CSV record.
     */
    protected NewSingleRecord mapFrom(CSVRecordWrapper csvRecord, Integer accountId) {
        NewSingleRecord newSingleRecord = new NewSingleRecord();
        newSingleRecord.setAccountId(accountId);
        newSingleRecord.setDescription(csvRecord.get("description").trim());
        newSingleRecord.setBankStatementId(csvRecord.get("description").trim());
        newSingleRecord.setRecordDate(
            LocalDate.parse(csvRecord.get("record_date"), this.dateTimeFormatter)
        );

        String transactionType = csvRecord.get("transaction_type").toLowerCase().trim();
        BigDecimal recordValue = new BigDecimal(csvRecord.get(transactionType));
        if (transactionType.equals("debit")) {
            recordValue = recordValue.negate();
        }
        newSingleRecord.setRecordValue(recordValue);
        newSingleRecord.setSourceRecordHash(
            this.hashService.calculateMD5(
                csvRecord.toMap().toString()
            )
        );
        LOGGER.debug("Parser CSV record: {}", newSingleRecord.toString());
        return newSingleRecord;
    }

    /**
     * Filters the list of mapped records until find the last previously imported record based on its hash, and returns
     * only the records not yet imported.
     *
     * @param records                   List of records to be filtered.
     * @param lastImportedRecordHash    Last previously imported record hash.
     * @return List containing the records not imported yet.
     */
    protected List<NewSingleRecord> filterNonImportedRecords(List<NewSingleRecord> records, String lastImportedRecordHash) {
        List<NewSingleRecord> nonImportedRecords = new ArrayList<>();
        for(NewSingleRecord record: records) {
            if (record.getSourceRecordHash().equals(lastImportedRecordHash)) {
                break;
            }
            nonImportedRecords.add(record);
        }
        return Collections.unmodifiableList(nonImportedRecords);
    }

}
