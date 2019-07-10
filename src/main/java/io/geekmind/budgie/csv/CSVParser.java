package io.geekmind.budgie.csv;

import io.geekmind.budgie.model.dto.NewImportedRecord;

import java.util.List;

/**
 * Defines the common features for any class that provides the capability to parse a CSV file into
 * balance records.
 *
 * @author Andre Silva
 */
@FunctionalInterface
public interface CSVParser {
    /**
     * Parse the content of the uploaded file into new instances of {@link NewImportedRecord}.
     * It skips the first line of the tile and any other record that has less than 3 (three)
     * fields.
     *
     * @param importedFile Content of the uploaded file.
     * @return List of {@link NewImportedRecord} filled in with the records parsed from the uploaded file.
     */
    List<NewImportedRecord> parseImportedFile(final byte[] importedFile);
}
