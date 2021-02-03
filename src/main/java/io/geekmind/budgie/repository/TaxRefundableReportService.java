package io.geekmind.budgie.repository;

import io.geekmind.budgie.model.dto.ExistingRecord;
import io.geekmind.budgie.model.dto.category.ExistingCategory;
import io.geekmind.budgie.model.dto.report.TaxRefundableCategorySummary;
import io.geekmind.budgie.model.dto.report.TaxRefundableReportRequest;
import io.geekmind.budgie.model.dto.standard_account.ExistingStandardAccount;
import io.geekmind.budgie.model.entity.Category;
import io.geekmind.budgie.model.entity.SingleRecord;
import io.geekmind.budgie.model.entity.StandardAccount;
import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;
import org.springframework.util.comparator.Comparators;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TaxRefundableReportService {

    private final MapperFacade mapperFacade;
    private final SingleRecordRepository singleRecordRepository;
    private final StandardAccountRepository standardAccountRepository;

    public TaxRefundableReportService(MapperFacade mapperFacade, SingleRecordRepository singleRecordRepository,
                                      StandardAccountRepository standardAccountRepository) {
        this.mapperFacade = mapperFacade;
        this.singleRecordRepository = singleRecordRepository;
        this.standardAccountRepository = standardAccountRepository;
    }

    public List<TaxRefundableCategorySummary> generateTaxRefundableSummaries(TaxRefundableReportRequest request) {
        List<SingleRecord> records = this.singleRecordRepository.findByAccountIdAndRecordDateBetweenAndIsTaxRefundableTrue(
            request.getExistingAccountId(), request.getStartDate(), request.getEndDate()
        );

        ExistingStandardAccount account = this.standardAccountRepository.findById(request.getExistingAccountId())
            .map((StandardAccount standardAccount) -> this.mapperFacade.map(standardAccount, ExistingStandardAccount.class))
            .orElse(null);

        Map<Category, List<SingleRecord>> recordsGroupedByCategory = records.stream().collect(
            Collectors.groupingBy(
                (SingleRecord record) -> record.getCategory(), Collectors.toList()
            )
        );

        return recordsGroupedByCategory.entrySet().stream()
            .map((Map.Entry<Category, List<SingleRecord>> entry) -> {
                ExistingCategory category = this.mapperFacade.map(entry.getKey(), ExistingCategory.class);
                List<ExistingRecord> existingRecords = entry.getValue().stream()
                    .map((SingleRecord record) -> this.mapperFacade.map(record, ExistingRecord.class))
                    .sorted(Comparator.comparing(ExistingRecord::getRecordDate)).collect(Collectors.toList());
                BigDecimal balance = existingRecords.stream()
                    .map(ExistingRecord::getRecordValue)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

                return new TaxRefundableCategorySummary(account, category, balance, existingRecords);
            })
            .sorted(Comparator.comparing((TaxRefundableCategorySummary o) -> o.getCategory().getName()))
            .collect(Collectors.toList());

    }


}
