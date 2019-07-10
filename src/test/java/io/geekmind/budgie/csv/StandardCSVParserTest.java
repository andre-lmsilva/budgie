package io.geekmind.budgie.csv;

import io.geekmind.budgie.digest.MessageDigestService;
import io.geekmind.budgie.model.dto.NewImportedRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class StandardCSVParserTest {

    @Mock
    private MessageDigestService messageDigestService;

    @Spy
    @InjectMocks
    private StandardCSVParser parser;

    @Before
    public void setUp() {
        this.parser.initialize();
    }

    @Test
    public void extractLines_withNewLineCharacters_ReturnsArrayOfStringsSplitByTheNewLineCharacter() {
        String sampleText = "FirstLine\nSecondLine";
        String[] result = this.parser.extractLines(sampleText);
        assertThat(result).hasSize(2)
            .containsExactly("FirstLine", "SecondLine");
    }

    @Test
    public void extractLines_withoutNewLineCharacter_ReturnsSingleElementArray() {
        String sampleText = "FirstLineSecondLine";
        String[] result = this.parser.extractLines(sampleText);
        assertThat(result).hasSize(1)
            .containsExactly("FirstLineSecondLine");
    }

    @Test
    public void normalize_withLineWithoutComaInTheMiddleOfTextField_DoesNotPerformAnyReplacement() {
        String sampleText = "1,\"Text Field\",23.45";
        String result = this.parser.normalize(sampleText);
        assertThat(result).isEqualTo(sampleText);
    }

    @Test
    public void normalize_withLineContainingCOmaInTHeMiddleOfTextField_ReplacesItByDash() {
        String sampleText = "1,\"Text,Field\",23.45";
        String result = this.parser.normalize(sampleText);
        assertThat(result).isEqualTo("1,\"Text-Field\",23.45");
    }

    @Test
    public void extractDate_withDateFieldPresent_ReturnsParsedDate() {
        LocalDate now = LocalDate.now();
        String[] fields = new String[] { "", now.format(DateTimeFormatter.ofPattern("dd/MM/yy")) };

        LocalDate result = this.parser.extractDate(fields);

        assertThat(result).isEqualTo(now);
    }

    @Test
    public void extractDescription_withDelimitedTextField_ReturnsWithDelimitersAndTrailingStrippedOut() {
        String sampleText = "\"Fake Record Description \"";
        String[] fields = new String[] { "", "", sampleText };

        String result = this.parser.extractDescription(fields);

        assertThat(result).isEqualTo("Fake Record Description");
    }

    @Test
    public void extractValue_withCreditDebitRecord_ReturnsItsValueNegated() {
        String[] fields = new String[] { "", "", "", "3.14", "Debit" };
        BigDecimal result = this.parser.extractValue(fields);
        assertThat(result).isEqualTo(BigDecimal.valueOf(-3.14D));
    }

    @Test
    public void extractValue_withDebitDebitRecord_ReturnsItsValueAsItIs() {
        String[] fields = new String[] { "", "", "", "", "3.14", "Credit" };
        BigDecimal result = this.parser.extractValue(fields);
        assertThat(result).isEqualTo(BigDecimal.valueOf(3.14D));
    }

    @Test
    public void extractValue_withUnknownRecordType_ReturnsZero() {
        String[] fields = new String[] { "", "", "", "", "3.14", "" };
        BigDecimal result = this.parser.extractValue(fields);
        assertThat(result).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    public void extractRecordHash_withNoPreCondition_CalculatedTheRecordHash() {
        doReturn("fakeHash")
            .when(this.messageDigestService).md5Digest(any());

        String result = this.parser.extractRecordHash("fake line");

        assertThat(result).isEqualTo("fakeHash");
    }

    @Test
    public void parseRecord_withCSVLine_ReturnsFilledInInstanceOfNewImportedRecord() {
        LocalDate today = LocalDate.now();
        doReturn(today).when(this.parser).extractDate(any());
        doReturn("Fake Description").when(this.parser).extractDescription(any());
        doReturn(BigDecimal.TEN).when(this.parser).extractValue(any());
        doReturn("Fake Hash").when(this.parser).extractRecordHash(any());

        NewImportedRecord result = this.parser.parseRecord("09/07/19,\"FakeCSV Line\"");

        assertThat(result)
            .hasFieldOrPropertyWithValue("recordDate", today)
            .hasFieldOrPropertyWithValue("description", "Fake Description")
            .hasFieldOrPropertyWithValue("recordValue", BigDecimal.TEN)
            .hasFieldOrPropertyWithValue("md5RecordHash", "Fake Hash");

    }

    @Test
    public void parseImportedFile_withCSVContent_ParsesEachLine() {
        String[] fakeNormalizedLines = new String[] {
                "Date,Description,Value,Type",
                "09/07/19,\"FakeRecordFirstRecord\",1.00,Debit",
                "09/07/19,\"FakeRecordWithLessThan3Fields",
                "09/07/19,\"FakeDebitRecord\",9.34,Debit"
        };
        doReturn(fakeNormalizedLines).when(this.parser).extractLines(anyString());
        doReturn("fakeNormalizedString").when(this.parser).normalize(anyString());
        NewImportedRecord fakeFirstRecord = new NewImportedRecord();
        NewImportedRecord fakeSecondRecord = new NewImportedRecord();

        doReturn(fakeFirstRecord, fakeSecondRecord)
            .when(this.parser).parseRecord(anyString());

        List<NewImportedRecord> result = this.parser.parseImportedFile("".getBytes());

        assertThat(result).containsExactly(fakeFirstRecord, fakeSecondRecord);
        verify(this.parser, times(2)).parseRecord(anyString());
    }
}