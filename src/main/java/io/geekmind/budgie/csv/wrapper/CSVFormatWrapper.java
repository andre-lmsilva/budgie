package io.geekmind.budgie.csv.wrapper;

import org.apache.commons.csv.CSVFormat;

import java.io.IOException;
import java.io.Reader;

public class CSVFormatWrapper {

    private final CSVFormat csvFormat;

    public CSVFormatWrapper(CSVFormat csvFormat) {
        this.csvFormat = csvFormat;
    }

    public CSVParserWrapper parse(Reader content) throws IOException {
        return new CSVParserWrapper(this.csvFormat.parse(content));
    }

}
