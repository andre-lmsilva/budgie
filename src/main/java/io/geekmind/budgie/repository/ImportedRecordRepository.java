package io.geekmind.budgie.repository;

import io.geekmind.budgie.model.entity.ImportedRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImportedRecordRepository extends JpaRepository<ImportedRecord, Integer> {

    Optional<ImportedRecord> findBySourceMd5Hash(String hash);

}
