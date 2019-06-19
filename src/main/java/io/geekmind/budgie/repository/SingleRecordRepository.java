package io.geekmind.budgie.repository;

import io.geekmind.budgie.model.entity.SingleRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SingleRecordRepository extends JpaRepository<SingleRecord, Integer> {
}
