package io.geekmind.budgie.repository;

import io.geekmind.budgie.model.dto.ExistingRecord;
import io.geekmind.budgie.model.dto.NewBudgetTemplateRecord;
import io.geekmind.budgie.model.entity.BudgetTemplateRecord;
import io.geekmind.budgie.model.entity.BudgetTemplateRecordContainer;
import ma.glasnost.orika.MapperFacade;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@CacheConfig(cacheNames = "budgetTemplateRecords")
public class BudgetTemplateRecordService {

    private final MapperFacade mapper;
    private final BudgetTemplateRecordRepository budgetTemplateRecordRepository;
    private final BudgetTemplateRecordContainerService budgetTemplateRecordContainerService;

    public BudgetTemplateRecordService(MapperFacade mapper,
                                       BudgetTemplateRecordRepository budgetTemplateRecordRepository,
                                       BudgetTemplateRecordContainerService budgetTemplateRecordContainerService) {
        this.mapper = mapper;
        this.budgetTemplateRecordRepository = budgetTemplateRecordRepository;
        this.budgetTemplateRecordContainerService = budgetTemplateRecordContainerService;
    }

    @Cacheable(key = "#accountId")
    public List<ExistingRecord> loadAllFromAccount(Integer accountId) {
        return this.budgetTemplateRecordRepository.findByAccount_Id(accountId)
            .stream()
            .map(record -> this.mapper.map(record, ExistingRecord.class))
            .sorted(Comparator.comparing(ExistingRecord::getRecordDate))
            .collect(Collectors.toList());
    }

    @CachePut(key = "#result.id")
    public ExistingRecord create(NewBudgetTemplateRecord newBudgetTemplateRecord) {
        BudgetTemplateRecordContainer container = this.mapper.map(newBudgetTemplateRecord, BudgetTemplateRecordContainer.class);
        container.addRecord(
            this.mapper.map(newBudgetTemplateRecord, BudgetTemplateRecord.class)
        );

        return this.mapper.map(
            this.budgetTemplateRecordContainerService.save(container).getRecords().get(0),
            ExistingRecord.class
        );
    }

    @CacheEvict(key = "#id")
    public Optional<ExistingRecord> remove(Integer id) {
        return this.budgetTemplateRecordRepository.findById(id)
            .map(record -> {
                if (record.getRecordContainer().getRecords().size() == 1) {
                    this.budgetTemplateRecordContainerService.remove(record.getRecordContainer().getId());
                } else {
                    this.budgetTemplateRecordRepository.delete(record);
                }
                return this.mapper.map(record, ExistingRecord.class);
            });
    }

    public Optional<ExistingRecord> loadById(Integer id) {
        return this.budgetTemplateRecordRepository.findById(id)
            .map(record -> this.mapper.map(record, ExistingRecord.class));
    }
}
