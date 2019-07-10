package io.geekmind.budgie.csv;

import io.geekmind.budgie.digest.MessageDigestService;
import io.geekmind.budgie.model.dto.NewImportedRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Provides the capability to parse a CSV file containing balance records.
 *
 * @author Andre Silva
 */
@Service
public class StandardCSVParser implements CSVParser {

    private static final String RECORD_DATE_PATTERN = "dd/MM/yy";
    private static final String TEXT_FIELD_DELIMITER = "\"";
    
    private static final String DEBIT_RECORD_IDENTIFIER = "Debit";
    private static final String CREDIT_RECORD_IDENTIFIER = "Credit";

    private static final int DATE_FIELD_OFFSET = 1;
    private static final int DESCRIPTION_FIELD_OFFSET = 2;
    private static final int DEBIT_RECORD_FIELD_OFFSET = 3;
    private static final int CREDIT_RECORD_FIELD_OFFSET = 4;

    private final MessageDigestService messageDigestService;
    private Pattern innerTextFieldCommaPattern;

    @Autowired
    public StandardCSVParser(MessageDigestService messageDigestService) {
        this.messageDigestService = messageDigestService;
    }

    /**
     * Pre-compile the regex pattern used by the {@link #normalize(String)} method to find
     * coma characters in the middle of text fields.
     */
    @PostConstruct
    public void initialize() {
        this.innerTextFieldCommaPattern = Pattern.compile("\".*,.*\"");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<NewImportedRecord> parseImportedFile(final byte[] importedFile) {
        String[] lines = this.extractLines(
            this.normalize(new String(importedFile, StandardCharsets.UTF_8))
        );
        return Stream.of(lines)
            .skip(1)
            .filter(line -> line.split(",").length > 3)
            .map(this::parseRecord)
            .collect(Collectors.toList());
    }

    /**
     * Splits the received text into an array of strings, using the new line character as delimiter.
     * @param data  Text to be split into lines.
     * @return Array of strings containing the segments delimited by the new line character.
     */
    protected String[] extractLines(String data) {
        return data.split("\n");
    }

    /**
     * Normalizes the line before split it into fields, replacing any coma (,) character
     * in the middle of text fields by dash (-) character.
     *
     * @param line  Line of text to be normalized.
     * @return Normalized line of text.
     */
    protected String normalize(String line) {
        Matcher matcher = this.innerTextFieldCommaPattern.matcher(line);
        String result = line;
        while(matcher.find()) {
            String fragment = matcher.group().replace(",", "-");
            result = line.replace(matcher.group(), fragment);
        }
        return result;
    }

    /**
     * Parses a line of the CSV file, extracting its field values and filling in a new
     * instance of {@link NewImportedRecord}.
     * @param line  Line to be parsed.
     * @return New instance of {@link NewImportedRecord} filled in with the line field values.
     */
    protected NewImportedRecord parseRecord(String line) {
        NewImportedRecord rawImportedRecord = new NewImportedRecord();
        String[] fields = line.split(",");
        rawImportedRecord.setRecordDate(this.extractDate(fields));
        rawImportedRecord.setDescription(this.extractDescription(fields));
        rawImportedRecord.setRecordValue(this.extractValue(fields));
        rawImportedRecord.setMd5RecordHash(this.extractRecordHash(line));
        return rawImportedRecord;
    }

    /**
     * Extracts the value of the date field, parses it to a {@link LocalDate} instance
     * and returns it.
     * @param fields    Line fields.
     * @return Parsed date field value.
     */
    protected LocalDate extractDate(String[] fields) {
        return LocalDate.parse(
            fields[DATE_FIELD_OFFSET].trim(),
            DateTimeFormatter.ofPattern(RECORD_DATE_PATTERN)
        );
    }

    /**
     * Extracts the record description field, strips the quote (") characters and any
     * trailing space from the beginning and the end of the value.
     * @param fields    Line fields.
     * @return Record description field.
     */
    protected String extractDescription(String[] fields) {
        return fields[DESCRIPTION_FIELD_OFFSET]
            .replaceAll(TEXT_FIELD_DELIMITER, "")
            .trim();
    }

    /**
     * Extracts the record value based on the field type. If it is a debit record, the
     * value will be extracted, converted to {@link BigDecimal} and negated. If it is
     * a credit record, the value will be extracted and converted to {@link BigDecimal}
     * as it is. If it is a different record type or if it is not possible to identify
     * it, returns {@link BigDecimal#ZERO}.
     *
     * @param fields Line fields.
     * @return Record value parsed to {@link BigDecimal} type.
     */
    protected BigDecimal extractValue(String[] fields) {
        int indexOfRecordType = fields.length - 1;
        String recordType = fields[indexOfRecordType].trim();
        BigDecimal recordValue = BigDecimal.ZERO;

        if (DEBIT_RECORD_IDENTIFIER.equals(recordType)) {

            recordValue = BigDecimal.valueOf(
                new Double(fields[DEBIT_RECORD_FIELD_OFFSET].trim())
            ).negate();

        } else if (CREDIT_RECORD_IDENTIFIER.equals(recordType)) {

            recordValue = BigDecimal.valueOf(
                new Double(fields[CREDIT_RECORD_FIELD_OFFSET].trim())
            );

        }
        return recordValue;
    }

    /**
     * Calculates the MD5 hash of the line. It delegates to {@link MessageDigestService#md5Digest(byte[])}
     * method to calculate the hash.
     *
     * @param line  Line to have its MD5 hash calculated.
     * @return Line's MD5 hash.
     */
    protected String extractRecordHash(String line) {
        return this.messageDigestService.md5Digest(line.getBytes());
    }
}