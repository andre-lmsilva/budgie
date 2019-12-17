package io.geekmind.budgie.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ImportCSVRecords implements Serializable {

    private List<NewSingleRecord> csvRecords;

}
