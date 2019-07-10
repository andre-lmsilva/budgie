package io.geekmind.budgie.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class ImportedRecord extends Record {

    @NotNull
    @Type(type = "encryptedTextField")
    private String sourceMd5Hash;

    @ManyToOne
    private ImportedFile importedFile;

}
