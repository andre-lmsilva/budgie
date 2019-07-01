package io.geekmind.budgie.repository;

import io.geekmind.budgie.model.dto.ExistingContainer;
import io.geekmind.budgie.model.dto.ExistingRecord;
import io.geekmind.budgie.model.dto.NewTransfer;
import io.geekmind.budgie.model.entity.Account;
import io.geekmind.budgie.model.entity.Category;
import io.geekmind.budgie.model.entity.TransferContainer;
import io.geekmind.budgie.model.entity.TransferRecord;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class TransferService {

    private final TransferRepository transferRepository;
    private final MapperFacade mapper;

    @Autowired
    public TransferService(TransferRepository transferRepository,
                           MapperFacade mapperFacade) {
        this.transferRepository = transferRepository;
        this.mapper = mapperFacade;
    }

    public void create(NewTransfer newTransfer) {
        TransferContainer transfer = this.mapper.map(newTransfer, TransferContainer.class);
        TransferRecord sourceRecord = new TransferRecord();
        sourceRecord.setRecordDate(newTransfer.getTransferDate());
        sourceRecord.setAccount(new Account());
        sourceRecord.getAccount().setId(newTransfer.getSourceAccountId());
        sourceRecord.setCategory(new Category());
        sourceRecord.getCategory().setId(newTransfer.getCategoryId());
        sourceRecord.setDescription(newTransfer.getDescription());
        sourceRecord.setRecordValue(newTransfer.getTransferValue().multiply(BigDecimal.valueOf(-1D)));
        transfer.addRecord(sourceRecord);

        TransferRecord destinationRecord = new TransferRecord();
        destinationRecord.setRecordDate(newTransfer.getTransferDate());
        destinationRecord.setAccount(new Account());
        destinationRecord.getAccount().setId(newTransfer.getDestinationAccountId());
        destinationRecord.setCategory(new Category());
        destinationRecord.getCategory().setId(newTransfer.getCategoryId());
        destinationRecord.setDescription(newTransfer.getDescription());
        destinationRecord.setRecordValue(newTransfer.getTransferValue());
        transfer.addRecord(destinationRecord);

        this.transferRepository.save(transfer);
    }

    public Optional<ExistingContainer> remove(Integer id) {
        return this.transferRepository.findById(id)
            .map(container -> {
                this.transferRepository.delete(container);
                return this.mapper.map(container, ExistingContainer.class);
            });
    }

}
