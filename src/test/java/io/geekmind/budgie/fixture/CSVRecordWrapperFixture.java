package io.geekmind.budgie.fixture;

import io.geekmind.budgie.csv.wrapper.CSVRecordWrapper;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class CSVRecordWrapperFixture {

    public static CSVRecordWrapper zero() {
        CSVRecordWrapper csvRecordWrapper = mock(CSVRecordWrapper.class);
        doReturn("BALANCE").when(csvRecordWrapper).get("description");
        doReturn("13/01/21").when(csvRecordWrapper).get("record_date");
        doReturn("CREDIT").when(csvRecordWrapper).get("transaction_type");
        doReturn("0.0").when(csvRecordWrapper).get("credit");
        return csvRecordWrapper;
    }

    public static CSVRecordWrapper credit() {
        CSVRecordWrapper csvRecordWrapper = mock(CSVRecordWrapper.class);
        doReturn("MONTHLY SALARY").when(csvRecordWrapper).get("description");
        doReturn("13/01/21").when(csvRecordWrapper).get("record_date");
        doReturn("CREDIT").when(csvRecordWrapper).get("transaction_type");
        doReturn("1000.0").when(csvRecordWrapper).get("credit");
        return csvRecordWrapper;
    }

    public static CSVRecordWrapper debit() {
        CSVRecordWrapper csvRecordWrapper = mock(CSVRecordWrapper.class);
        doReturn("CAR LOAN").when(csvRecordWrapper).get("description");
        doReturn("21/01/21").when(csvRecordWrapper).get("record_date");
        doReturn("DEBIT").when(csvRecordWrapper).get("transaction_type");
        doReturn("200.0").when(csvRecordWrapper).get("debit");
        return csvRecordWrapper;
    }

}
