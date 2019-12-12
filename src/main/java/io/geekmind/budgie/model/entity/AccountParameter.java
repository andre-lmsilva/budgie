package io.geekmind.budgie.model.entity;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Data
public class AccountParameter implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @NotNull
    private Account account;

    @Column(name = "parameter_key")
    @Type(type = "encryptedTextField")
    @NotNull
    @Pattern(regexp = "[a-zA-Z0-9_-]*")
    @Size(min = 5)
    private String key;

    @Column(name = "parameter_value")
    @Type(type = "encryptedTextField")
    @NotNull
    private String value;

}
