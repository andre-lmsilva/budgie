package io.geekmind.budgie.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExistingRecord {

    private Integer id;

    private ExistingCategory category;

    private ExistingAccount account;

    private LocalDate recordDate;

    private String description;

    private BigDecimal recordValue;

    private String recordType;

    private Integer containerId;
}
