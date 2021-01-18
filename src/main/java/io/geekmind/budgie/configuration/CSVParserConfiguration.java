package io.geekmind.budgie.configuration;

import io.geekmind.budgie.csv.wrapper.CSVFormatWrapper;
import org.apache.commons.csv.CSVFormat;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CSVParserConfiguration {

    public static final String AIB_BANK_ACCOUNT_CSV_FORMAT = "aibBankAccountCSVFormat";

    public static final String[] FILE_HEADERS = {
        "posted_account",
        "record_date",
        "description",
        "debit",
        "credit",
        "balance",
        "transaction_type"
    };

    @Bean(CSVParserConfiguration.AIB_BANK_ACCOUNT_CSV_FORMAT)
    public CSVFormatWrapper getAIBBankAccountParser() {
        return new CSVFormatWrapper(
            CSVFormat.DEFAULT.withSkipHeaderRecord().withHeader(FILE_HEADERS)
        );
    }

}
