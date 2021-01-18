package io.geekmind.budgie.csv.wrapper;

import org.apache.commons.csv.CSVParser;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class CSVParserWrapper {

    private final CSVParser csvParser;
    private List<CSVRecordWrapper> csvRecords;

    public CSVParserWrapper(CSVParser csvParser) {
        this.csvParser = csvParser;
    }

    public List<CSVRecordWrapper> getRecords() throws IOException {
        if (this.csvRecords == null) {
            this.csvRecords = this.csvParser.getRecords()
                .stream()
                .map(CSVRecordWrapper::new)
                .collect(Collectors.toList());
        }
        return csvRecords;
    }

}
