package io.geekmind.budgie.model.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "record_container_type")
public abstract class RecordContainer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Size(min = 5, max = 140)
    private String name;

    @OneToMany(mappedBy = "recordContainer", orphanRemoval = true, cascade = {CascadeType.ALL})
    @Size(min = 2)
    private List<ContainerRecord> records;

}
