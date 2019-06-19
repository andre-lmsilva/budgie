package io.geekmind.budgie.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Configuration
public class DateFormatterConfiguration {

    @Bean
    public Formatter<LocalDate> getLocalDateFormatter() {
        return new Formatter<LocalDate>() {
            @Override
            public LocalDate parse(String text, Locale locale) throws ParseException {
                return LocalDate.parse(text, DateTimeFormatter.ISO_DATE);
            }

            @Override
            public String print(LocalDate localDate, Locale locale) {
                return DateTimeFormatter.ISO_DATE.format(localDate);
            }
        };
    }

}
