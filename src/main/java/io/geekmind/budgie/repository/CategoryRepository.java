package io.geekmind.budgie.repository;

import io.geekmind.budgie.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
