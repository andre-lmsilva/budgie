package io.geekmind.budgie.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ExistingRecord {

    private Integer id;

    private ExistingCategory category;

    private ExistingAccount account;

    private LocalDate recordDate;

    private String description;

    private BigDecimal recordValue;

}
