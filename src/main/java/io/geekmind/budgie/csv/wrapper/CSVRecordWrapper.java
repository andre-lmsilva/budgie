package io.geekmind.budgie.csv.wrapper;

import org.apache.commons.csv.CSVRecord;

import java.util.Map;

public class CSVRecordWrapper {

    private final CSVRecord csvRecord;

    public CSVRecordWrapper(CSVRecord csvRecord) {
        this.csvRecord = csvRecord;
    }

    public String get(String key) {
        return csvRecord.get(key);
    }

    public Map<String, String> toMap() {
        return this.csvRecord.toMap();
    }

    public long getRecordNumber() {
        return this.csvRecord.getRecordNumber();
    }

}
