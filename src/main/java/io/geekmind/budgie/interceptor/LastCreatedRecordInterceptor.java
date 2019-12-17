package io.geekmind.budgie.interceptor;

import io.geekmind.budgie.model.dto.ExistingContainer;
import io.geekmind.budgie.model.dto.ExistingRecord;
import io.geekmind.budgie.model.entity.AccountParameterKey;
import io.geekmind.budgie.repository.AccountParameterService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Aspect
@Component
public class LastCreatedRecordInterceptor {

    private final AccountParameterService accountParameterService;

    public LastCreatedRecordInterceptor(AccountParameterService accountParameterService) {
        this.accountParameterService = accountParameterService;
    }

    @Pointcut("execution(* io.geekmind.budgie.repository.SingleRecordService.create(..))")
    private void whenCreatingSingleRecord() { }

    @Pointcut("execution(* io.geekmind.budgie.repository.InstalmentContainerService.create(..))")
    private void whenCreatingInstalmentRecord() {  }

    @Pointcut("execution(* io.geekmind.budgie.repository.TransferService.create(..))")
    private void whenCreatingTransferRecord() { }

    @Pointcut("execution(* io.geekmind.budgie.repository.BudgetRecordService.create(..))")
    private void whenApplyingBudgetTemplateRecord() { }

    @Around("whenCreatingSingleRecord()")
    public Object interceptSingleRecord(ProceedingJoinPoint joinPoint) throws Throwable {
        ExistingRecord result = (ExistingRecord)joinPoint.proceed();
        this.handleRecord(result);
        return result;
    }

    @Around("whenCreatingTransferRecord()")
    public Object interceptTransferRecord(ProceedingJoinPoint joinPoint) throws Throwable {
        ExistingContainer result = (ExistingContainer)joinPoint.proceed();
        result.getRecords().forEach(this::handleRecord);
        return result;
    }

    @Around("whenCreatingInstalmentRecord()")
    public Object interceptInstalmentRecord(ProceedingJoinPoint joinPoint) throws Throwable {
        ExistingContainer result = (ExistingContainer)joinPoint.proceed();
        this.handleRecord(result.getRecords().get(0));
        return result;
    }

    @Around("whenApplyingBudgetTemplateRecord()")
    public Object interceptingBudgetRecord(ProceedingJoinPoint joinPoint) throws Throwable {
        Optional<ExistingRecord> result = (Optional<ExistingRecord>)joinPoint.proceed();
        result.ifPresent(this::handleRecord);
        return result;
    }

    public void handleRecord(ExistingRecord existingRecord) {
        this.accountParameterService.upsert(
            existingRecord.getAccount(),
            AccountParameterKey.LAST_CREATED_RECORD,
            existingRecord.getId().toString()
        );
    }
}
