package io.geekmind.budgie.repository;

import io.geekmind.budgie.model.entity.BudgetTemplateRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BudgetTemplateRecordRepository extends JpaRepository<BudgetTemplateRecord, Integer> {

    List<BudgetTemplateRecord> findByAccount_Id(Integer accountId);

}
