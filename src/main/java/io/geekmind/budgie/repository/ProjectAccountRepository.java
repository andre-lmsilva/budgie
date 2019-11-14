package io.geekmind.budgie.repository;

import io.geekmind.budgie.model.entity.ProjectAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectAccountRepository extends JpaRepository<ProjectAccount, Integer> {
}
