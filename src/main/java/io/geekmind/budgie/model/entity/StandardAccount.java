package io.geekmind.budgie.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class StandardAccount extends Account {

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private StandardAccount parent;

    @OneToMany(mappedBy = "parent", cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Account> dependants;

}
