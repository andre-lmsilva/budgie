package io.geekmind.budgie.repository;

import io.geekmind.budgie.model.entity.BudgetRecord;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Provides an abstract way to persist, retrieve and remove instances of {@link BudgetRecord}
 * from the database.
 *
 * @author Andre Silva
 */
public interface BudgetRecordRepository extends JpaRepository<BudgetRecord, Integer> {
}
