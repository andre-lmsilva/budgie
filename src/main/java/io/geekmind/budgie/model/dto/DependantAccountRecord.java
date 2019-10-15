package io.geekmind.budgie.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
public class DependantAccountRecord extends ExistingRecord {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate periodStartDate;
}
