package io.geekmind.budgie.repository;

import io.geekmind.budgie.model.entity.TransferContainer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferRepository extends JpaRepository<TransferContainer, Integer> {
}
