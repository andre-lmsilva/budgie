package io.geekmind.budgie.model.dto;

import lombok.Data;

@Data
public class ExistingImportedRecord extends ExistingRecord {

    private String sourceMd5Hash;

    private ExistingImportedFile importedFile;

}
