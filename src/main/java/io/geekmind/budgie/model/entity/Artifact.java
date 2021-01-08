package io.geekmind.budgie.model.entity;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@Entity
@Data
public class Artifact {

    @Id
    private Integer id;

    @MapsId
    @OneToOne(orphanRemoval = true, cascade = {CascadeType.ALL})
    @JoinColumn(name="id")
    @NotNull
    private Attachment attachment;

    @NotNull
    @Type(type = "encryptedBinaryField")
    private byte[] content;

}
