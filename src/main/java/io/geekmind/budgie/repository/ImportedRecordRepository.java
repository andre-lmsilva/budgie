package io.geekmind.budgie.repository;

import io.geekmind.budgie.model.entity.ImportedRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImportedRecordRepository extends JpaRepository<ImportedRecord, Integer> {

}
