package io.geekmind.budgie.repository;

import io.geekmind.budgie.model.entity.SingleRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface SingleRecordRepository extends JpaRepository<SingleRecord, Integer> {

    /**
     * Retrieves all records flagged as tax refundable, between the two received dates, for the received account.
     *
     * @param accountId Account's id.
     * @param startDate Start date of the range used as a criterion to select records based on its recordDate attribute.
     * @param endDate End date of the range used as a criterion to select records based on its recordDate attribute.
     *
     * @return List of matching records or an empty list when no record matches the received criteria.
     */
    List<SingleRecord> findByAccountIdAndRecordDateBetweenAndIsTaxRefundableTrue(Integer accountId, LocalDate startDate,
                                                                                 LocalDate endDate);

}
