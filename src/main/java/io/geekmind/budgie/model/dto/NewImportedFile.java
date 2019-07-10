package io.geekmind.budgie.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewImportedFile {

    @NotNull
    private Integer id;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime importedAt;

    @NotNull
    private String fileName;

    @NotNull
    private String md5FileHash;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;

    @NotNull
    @Size(min = 1)
    @Valid
    public List<NewImportedRecord> newImportedRecords;

    public void addRecord(NewImportedRecord record) {
        if (null == newImportedRecords) {
            this.newImportedRecords = new ArrayList<>();
        }
        record.setImportedFileId(this.id);
        this.newImportedRecords.add(record);
    }

}
