package io.geekmind.budgie.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExistingContainer {

    private Integer id;

    private String name;

    private List<ExistingRecord> records;

}
