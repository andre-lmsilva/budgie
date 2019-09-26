package io.geekmind.budgie.repository;

import io.geekmind.budgie.model.entity.BudgetTemplateRecordContainer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetTemplateRecordContainerRepository extends JpaRepository<BudgetTemplateRecordContainer, Integer> {
}
