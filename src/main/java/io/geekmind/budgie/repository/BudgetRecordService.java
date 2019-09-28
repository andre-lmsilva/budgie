package io.geekmind.budgie.repository;

import io.geekmind.budgie.model.dto.ExistingRecord;
import io.geekmind.budgie.model.entity.BudgetRecord;
import io.geekmind.budgie.model.entity.BudgetTemplateRecordContainer;
import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

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

                return this.mapper.map(
                    this.budgetRecordRepository.save(budgetRecord),
                    ExistingRecord.class
                );
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

}
