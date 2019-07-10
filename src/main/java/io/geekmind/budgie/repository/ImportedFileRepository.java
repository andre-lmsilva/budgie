package io.geekmind.budgie.repository;

import io.geekmind.budgie.model.entity.ImportedFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImportedFileRepository extends JpaRepository<ImportedFile, Integer> {
}
