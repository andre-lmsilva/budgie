package io.geekmind.budgie.repository;

import io.geekmind.budgie.model.dto.ExistingContainer;
import io.geekmind.budgie.model.dto.NewInstalment;
import io.geekmind.budgie.model.entity.InstalmentContainer;
import io.geekmind.budgie.model.entity.InstalmentRecord;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class InstalmentContainerService {

    private final InstalmentContainerRepository instalmentContainerRepository;
    private final MapperFacade mapper;

    @Autowired
    public InstalmentContainerService(InstalmentContainerRepository instalmentContainerRepository,
                                      MapperFacade mapper) {
        this.instalmentContainerRepository = instalmentContainerRepository;
        this.mapper = mapper;
    }

    public ExistingContainer create(NewInstalment newInstalment) {
        InstalmentContainer container = this.mapper.map(newInstalment, InstalmentContainer.class);
        for(int index = 0; index < newInstalment.getNumberOfInstalments(); index++) {
            LocalDate instalmentDate = newInstalment.getStartingAt().plusMonths(index);
            InstalmentRecord record = this.mapper.map(newInstalment, InstalmentRecord.class);
            record.setRecordDate(instalmentDate);
            record.setDescription(
                String.format(record.getDescription(), index+1)
            );
            container.addRecord(record);
        }
        return this.mapper.map(
            this.instalmentContainerRepository.save(container),
            ExistingContainer.class
        );
    }

    public Optional<ExistingContainer> remove(Integer id) {
        return this.instalmentContainerRepository.findById(id)
            .map(container -> {
                this.instalmentContainerRepository.delete(container);
                return this.mapper.map(container, ExistingContainer.class);
            });
    }

}
