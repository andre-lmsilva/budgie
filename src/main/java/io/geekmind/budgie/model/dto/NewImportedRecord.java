package io.geekmind.budgie.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewImportedRecord {

    @NotNull
    private Integer importedFileId;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate recordDate;

    @NotNull
    @Size(min = 5, max = 140)
    private String description;

    @NotNull
    private BigDecimal recordValue;

    @NotNull
    @Min(0)
    private Integer categoryId;

    private Integer accountId;

    @NotNull
    private String md5RecordHash;

}
