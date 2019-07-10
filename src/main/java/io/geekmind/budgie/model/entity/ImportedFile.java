package io.geekmind.budgie.model.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class ImportedFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private LocalDateTime importedAt;

    @NotNull
    private String fileName;

    @NotNull
    private String fileMimeType;

    @NotNull
    private String md5FileHash;

    @NotNull
    private Long fileSize;

    @NotNull
    private String encodedContent;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

}
