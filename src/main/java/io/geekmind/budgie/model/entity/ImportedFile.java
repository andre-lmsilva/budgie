package io.geekmind.budgie.model.entity;

import lombok.Data;
import org.hibernate.annotations.Type;

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
    @Type(type = "encryptedTextField")
    private String fileName;

    @NotNull
    @Type(type = "encryptedTextField")
    private String fileMimeType;

    @NotNull
    @Type(type = "encryptedTextField")
    private String md5FileHash;

    @NotNull
    private Long fileSize;

    @NotNull
    @Type(type = "encryptedTextField")
    private String encodedContent;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

}
