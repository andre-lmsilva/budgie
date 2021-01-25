package io.geekmind.budgie.csv;

import io.geekmind.budgie.csv.wrapper.CSVFormatWrapper;
import io.geekmind.budgie.csv.wrapper.CSVParserWrapper;
import io.geekmind.budgie.csv.wrapper.CSVRecordWrapper;
import io.geekmind.budgie.fixture.CSVRecordWrapperFixture;
import io.geekmind.budgie.fixture.ExistingAccountParameterFixture;
import io.geekmind.budgie.fixture.ExistingStandardAccountFixture;
import io.geekmind.budgie.fixture.NewSingleRecordFixture;
import io.geekmind.budgie.model.dto.NewSingleRecord;
import io.geekmind.budgie.model.dto.account.ExistingAccount;
import io.geekmind.budgie.model.dto.account_parameter.ExistingAccountParameter;
import io.geekmind.budgie.model.dto.standard_account.ExistingStandardAccount;
import io.geekmind.budgie.model.entity.AccountParameterKey;
import io.geekmind.budgie.repository.AccountParameterService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.Silent.class)
public class CSVParserServiceTest {

    @Mock
    private HashService hashService;

    @Mock
    private AccountParameterService accountParameterService;

    @Mock
    private CSVFormatWrapper aibBankAccountCSVFormat;

    @Spy
    @InjectMocks
    private CSVParserService service;

    @Before
    public void setUp() {
        this.service.initialize();
    }

    @Test
    public void readCSVContent_WithIOExceptionAccessingMultipartFile_ThrowsCSVParsingException() throws IOException {
        IOException fakeException = new IOException("Fake Exception");
        MultipartFile content = mock(MultipartFile.class);
        doThrow(fakeException).when(content).getBytes();

        assertThatThrownBy(() -> this.service.readCSVContent(content))
            .isInstanceOf(CSVParsingException.class)
            .hasCause(fakeException)
            .hasMessage("It was not possible to read the content of the uploaded file due the following exception: " +
                "Fake Exception");
    }

    @Test
    public void readCSVContent_WithNoIOExceptionAccessingMultipartFile_ReturnsCSVContentAsString() throws IOException {
        MultipartFile content = mock(MultipartFile.class);
        doReturn("Fake;CSV".getBytes()).when(content).getBytes();

        String result = this.service.readCSVContent(content);

        assertThat(result).isEqualTo("Fake;CSV");
    }

    @Test
    public void getCSVParser_WithIOExceptionToParseTheCSVContent_ThrowsCSVParsingException() throws IOException {
        MultipartFile content = mock(MultipartFile.class);
        doReturn("Field1;Field2").when(this.service).readCSVContent(content);
        IOException fakeException = new IOException("Fake Exception");
        doThrow(fakeException).when(this.aibBankAccountCSVFormat).parse(any(StringReader.class));

        assertThatThrownBy(() -> this.service.getCSVParser(content))
            .isInstanceOf(CSVParsingException.class)
            .hasCause(fakeException)
            .hasMessage("It was not possible to parse the received CSV file due the following exception: Fake Exception");
    }

    @Test
    public void getCSVParser_WithNoIOExceptionToParseTheCSVContent_ReturnsCSVParser() throws IOException {
        MultipartFile content = mock(MultipartFile.class);
        doReturn("Field1;Field2").when(this.service).readCSVContent(content);
        CSVParserWrapper csvParserWrapper = new CSVParserWrapper(null);
        doReturn(csvParserWrapper).when(this.aibBankAccountCSVFormat).parse(any(StringReader.class));

        CSVParserWrapper result = this.service.getCSVParser(content);

        assertThat(result).isSameAs(csvParserWrapper);
    }

    @Test
    public void getLastImportedRecordHash_WithNoLastImportedRecord_ReturnsEmptyString() {
        ExistingAccount account = ExistingStandardAccountFixture.getMainAccount();
        doReturn(Optional.empty()).when(this.accountParameterService)
            .loadByAccountAndKey(account, AccountParameterKey.MOST_RECENTLY_CSV_RECORD_IMPORTED);

        String result = this.service.getLastImportedRecordHash(account);

        assertThat(result).isEmpty();
    }

    @Test
    public void getLastImportedRecordHash_WithLastImportedRecord_ReturnsItsValue() {
        ExistingAccount account = ExistingStandardAccountFixture.getMainAccount();
        ExistingAccountParameter parameter = ExistingAccountParameterFixture.mostRecentlyCSVRecordImported();
        doReturn(Optional.of(parameter)).when(this.accountParameterService)
            .loadByAccountAndKey(account, AccountParameterKey.MOST_RECENTLY_CSV_RECORD_IMPORTED);

        String result = this.service.getLastImportedRecordHash(account);

        assertThat(result).isEqualTo(parameter.getValue());
    }

    @Test
    public void mapFromList_WithEmptyList_ReturnsEmptyList() {
        List<NewSingleRecord> result = this.service.mapFrom(Collections.emptyList(), 1);
        assertThat(result).isEmpty();
    }

    @Test
    public void mapFromList_WithNonEmptyList_IgnoresZeroValuedRecords() {
        NewSingleRecord mappedCreditRecord = new NewSingleRecord();
        CSVRecordWrapper creditRecord = CSVRecordWrapperFixture.credit();
        doReturn(mappedCreditRecord).when(this.service).mapFrom(creditRecord, -1);

        NewSingleRecord mappedDebitRecord = new NewSingleRecord();
        CSVRecordWrapper debitRecord = CSVRecordWrapperFixture.debit();
        doReturn(mappedDebitRecord).when(this.service).mapFrom(debitRecord, -1);

        CSVRecordWrapper zeroValueRecord = CSVRecordWrapperFixture.zero();
        List<CSVRecordWrapper> records = Arrays.asList(creditRecord, debitRecord, zeroValueRecord);

        List<NewSingleRecord> result = this.service.mapFrom(records, -1);

        assertThat(result).containsOnly(mappedCreditRecord, mappedDebitRecord);
    }

    @Test
    public void mapFrom_WithCreditRecord_MapsCreditValue() {
        CSVRecordWrapper creditRecord = CSVRecordWrapperFixture.credit();
        doReturn("fakeHash").when(this.hashService).calculateMD5(anyString());
        doReturn(new HashMap<>()).when(creditRecord).toMap();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        NewSingleRecord result = this.service.mapFrom(creditRecord, -1);

        assertThat(result)
            .hasFieldOrPropertyWithValue("accountId", -1)
            .hasFieldOrPropertyWithValue("description", "MONTHLY SALARY")
            .hasFieldOrPropertyWithValue("bankStatementId", "MONTHLY SALARY")
            .hasFieldOrPropertyWithValue("recordDate", LocalDate.parse("13/01/2021", dateTimeFormatter))
            .hasFieldOrPropertyWithValue("sourceRecordHash", "fakeHash")
            .hasFieldOrPropertyWithValue("recordValue", BigDecimal.valueOf(1000.0D));
    }

    @Test
    public void mapFrom_WithDebitRecord_MapsDebitValue() {
        CSVRecordWrapper debitRecord = CSVRecordWrapperFixture.debit();
        doReturn("fakeDebitHash").when(this.hashService).calculateMD5(anyString());
        doReturn(new HashMap<>()).when(debitRecord).toMap();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yy");

        NewSingleRecord result = this.service.mapFrom(debitRecord, -1);

        assertThat(result)
            .hasFieldOrPropertyWithValue("accountId", -1)
            .hasFieldOrPropertyWithValue("description", "CAR LOAN")
            .hasFieldOrPropertyWithValue("bankStatementId", "CAR LOAN")
            .hasFieldOrPropertyWithValue("recordDate", LocalDate.parse("21/01/21", dateTimeFormatter))
            .hasFieldOrPropertyWithValue("sourceRecordHash", "fakeDebitHash")
            .hasFieldOrPropertyWithValue("recordValue", BigDecimal.valueOf(200.0D).negate());
    }

    @Test
    public void filterNonImportedRecords_WithEmptyRecordList_ReturnsEmptyList() {
        List<NewSingleRecord> records = Collections.emptyList();
        List<NewSingleRecord> result = this.service.filterNonImportedRecords(records, "fakeHash");
        assertThat(result).isEmpty();
    }

    @Test
    public void filterNonImportedRecords_WithNonExistingLastImportedRecordHash_ReturnsAllReceivedRecords() {
        List<NewSingleRecord> records = Arrays.asList(
            NewSingleRecordFixture.credit(), NewSingleRecordFixture.debit()
        );

        List<NewSingleRecord> result = this.service.filterNonImportedRecords(records, "NOT DEFINED");

        assertThat(result).containsOnly(records.get(0), records.get(1));
    }

    @Test
    public void filterNonImportedRecord_WithExistingLastImportedRecordHash_ReturnsOnlyRecordsOccurringAfterTheLastImportedRecord() {
        List<NewSingleRecord> records = Arrays.asList(
                NewSingleRecordFixture.credit(), NewSingleRecordFixture.debit()
        );

        List<NewSingleRecord> result = this.service.filterNonImportedRecords(
            records, records.get(1).getSourceRecordHash()
        );

        assertThat(result).containsOnly(records.get(0));
    }

    @Test
    public void parseCSVFile_WithExceptionToMapRecords_ThrowCSVParsingException() throws IOException {
        MultipartFile csvFile = mock(MultipartFile.class);
        ExistingStandardAccount existingStandardAccount = ExistingStandardAccountFixture.getSavingsAccount();
        CSVParserWrapper parser = mock(CSVParserWrapper.class);
        doReturn(parser).when(this.service).getCSVParser(csvFile);
        RuntimeException fakeException = new RuntimeException("Fake Exception");
        doThrow(fakeException).when(parser).getRecords();

        assertThatThrownBy(() -> this.service.parseCSVFile(csvFile, existingStandardAccount))
            .isInstanceOf(CSVParsingException.class)
            .hasCause(fakeException)
            .hasMessage("It was not possible to obtain the CSV records due the following exception: Fake Exception");
    }

    @Test
    public void parseCSVFile_WithNoException_ParsesAndMapsCSVRecords() throws IOException {
        MultipartFile csvFile = mock(MultipartFile.class);
        CSVParserWrapper parser = mock(CSVParserWrapper.class);
        doReturn(parser).when(this.service).getCSVParser(csvFile);

        List<CSVRecordWrapper> records = Collections.singletonList(CSVRecordWrapperFixture.credit());
        doReturn(records).when(parser).getRecords();

        List<NewSingleRecord> parsedRecords = Collections.singletonList(NewSingleRecordFixture.credit());
        ExistingStandardAccount existingStandardAccount = ExistingStandardAccountFixture.getSavingsAccount();
        doReturn(parsedRecords).when(this.service).mapFrom(records, existingStandardAccount.getId());

        doReturn("fakeHash").when(service).getLastImportedRecordHash(existingStandardAccount);
        List<NewSingleRecord> filteredRecord = Collections.singletonList(NewSingleRecordFixture.credit());
        doReturn(filteredRecord).when(this.service).filterNonImportedRecords(parsedRecords, "fakeHash");

        List<NewSingleRecord> result = this.service.parseCSVFile(csvFile, existingStandardAccount);

        assertThat(result).isSameAs(filteredRecord);
    }

}