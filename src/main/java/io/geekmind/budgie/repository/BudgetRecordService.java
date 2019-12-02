package io.geekmind.budgie.repository;

import io.geekmind.budgie.model.dto.ExistingRecord;
import io.geekmind.budgie.model.entity.BudgetRecord;
import io.geekmind.budgie.model.entity.BudgetTemplateRecord;
import io.geekmind.budgie.model.entity.BudgetTemplateRecordContainer;
import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BudgetRecordService {

    private final MapperFacade mapper;
    private final BudgetRecordRepository budgetRecordRepository;
    private final BudgetTemplateRecordService budgetTemplateRecordService;
    private final BudgetTemplateRecordContainerService budgetTemplateRecordContainerService;

    public BudgetRecordService(MapperFacade mapper,
                               BudgetRecordRepository budgetRecordRepository,
                               BudgetTemplateRecordService budgetTemplateRecordService,
                               BudgetTemplateRecordContainerService budgetTemplateRecordContainerService) {
        this.mapper = mapper;
        this.budgetRecordRepository = budgetRecordRepository;
        this.budgetTemplateRecordService = budgetTemplateRecordService;
        this.budgetTemplateRecordContainerService = budgetTemplateRecordContainerService;
    }

    public Optional<ExistingRecord> create(Integer budgetTemplateRecordId,
                                           LocalDate periodStartDate,
                                           LocalDate periodEndDate) {
        return this.budgetTemplateRecordService.loadById(budgetTemplateRecordId)
            .map(record -> {
                BudgetRecord budgetRecord = this.mapper.map(record, BudgetRecord.class);
                if (budgetRecord.getRecordDate().getDayOfMonth() >= periodStartDate.getDayOfMonth()) {
                    budgetRecord.setRecordDate(
                        LocalDate.of(periodStartDate.getYear(), periodStartDate.getMonth(), record.getRecordDate().getDayOfMonth())
                    );
                } else {
                    budgetRecord.setRecordDate(
                        LocalDate.of(periodEndDate.getYear(), periodEndDate.getMonth(), record.getRecordDate().getDayOfMonth())
                    );
                }
                budgetRecord.setRecordContainer(new BudgetTemplateRecordContainer());
                budgetRecord.getRecordContainer().setId(record.getContainerId());

                BudgetRecord persistedRecord = this.budgetRecordRepository.save(budgetRecord);

                return this.mapper.map(persistedRecord, ExistingRecord.class);
            });
    }

    @Transactional
    public Optional<ExistingRecord> remove(Integer budgetRecordId) {
        return this.budgetRecordRepository.findById(budgetRecordId)
            .map(record -> {
                if (record.getRecordContainer().getRecords().size() == 1) {
                    this.budgetTemplateRecordContainerService.remove(record.getRecordContainer().getId());
                } else {
                    record.getRecordContainer().getRecords().remove(record);
                    this.budgetTemplateRecordContainerService.save((BudgetTemplateRecordContainer)record.getRecordContainer());
                }
                return this.mapper.map(record, ExistingRecord.class);
            });
    }


    @Transactional
    public List<ExistingRecord> applyAllBudget(Integer accountId, LocalDate periodStartDate, LocalDate periodEndDate) {
        return this.budgetTemplateRecordService.loadAllFromAccount(accountId)
            .stream()
            .map(record -> this.create(record.getId(), periodStartDate, periodEndDate).orElse(null))
            .collect(Collectors.toList());
    }

}
