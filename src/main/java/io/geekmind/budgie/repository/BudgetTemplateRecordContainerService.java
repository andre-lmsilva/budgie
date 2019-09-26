package io.geekmind.budgie.repository;

import io.geekmind.budgie.model.dto.ExistingContainer;
import io.geekmind.budgie.model.entity.BudgetTemplateRecordContainer;
import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BudgetTemplateRecordContainerService {

    private final BudgetTemplateRecordContainerRepository budgetTemplateRecordContainerRepository;
    private final MapperFacade mapper;

    public BudgetTemplateRecordContainerService(BudgetTemplateRecordContainerRepository budgetTemplateRecordContainerRepository,
                                                MapperFacade mapper) {
        this.budgetTemplateRecordContainerRepository = budgetTemplateRecordContainerRepository;
        this.mapper = mapper;
    }

    public Optional<ExistingContainer> remove(Integer containerId) {
        return this.budgetTemplateRecordContainerRepository.findById(containerId)
            .map(container -> {
                this.budgetTemplateRecordContainerRepository.delete(container);
                return this.mapper.map(container, ExistingContainer.class);
            });
    }

    public ExistingContainer save(BudgetTemplateRecordContainer container) {
        return this.mapper.map(
            this.budgetTemplateRecordContainerRepository.save(container),
            ExistingContainer.class
        );
    }

}
