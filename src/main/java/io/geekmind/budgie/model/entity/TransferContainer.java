package io.geekmind.budgie.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import java.util.ArrayList;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class TransferContainer extends RecordContainer {

    public void addRecord(ContainerRecord containerRecord) {
        if (null == this.getRecords()) {
            this.setRecords(new ArrayList<>());
        }
        containerRecord.setRecordContainer(this);
        this.getRecords().add(containerRecord);
    }

}
