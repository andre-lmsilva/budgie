package io.geekmind.budgie.repository;

import io.geekmind.budgie.model.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface RecordRepository extends JpaRepository<Record, Integer> {

    List<Record> findByAccount_IdAndRecordDateBetweenOrderByRecordDate(Integer accountId, LocalDate startDate, LocalDate endDate);

}
