package io.geekmind.budgie.csv;

import io.geekmind.budgie.model.dto.ImportCSVRecords;
import io.geekmind.budgie.model.dto.NewSingleRecord;
import io.geekmind.budgie.model.entity.AccountParameterKey;
import io.geekmind.budgie.repository.AccountParameterService;
import io.geekmind.budgie.repository.StandardAccountService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Aspect
@Component
public class LastCSVImportedRecordInterceptor {

    private final AccountParameterService accountParameterService;
    private final StandardAccountService standardAccountService;

    public LastCSVImportedRecordInterceptor(AccountParameterService accountParameterService,
                                            StandardAccountService standardAccountService) {
        this.accountParameterService = accountParameterService;
        this.standardAccountService = standardAccountService;
    }

    @Around("execution(* io.geekmind.budgie.controller.CSVUploadController.importRecords(..))")
    public Object intercept(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();

        ImportCSVRecords records = (ImportCSVRecords)joinPoint.getArgs()[0];
        Optional<NewSingleRecord> mostRecentlyImportedRecord = records.getCsvRecords()
            .stream()
            .filter(record -> !StringUtils.isEmpty(record.getSourceRecordHash()))
            .findFirst();

        mostRecentlyImportedRecord.ifPresent(record ->
            this.accountParameterService.upsert(
                standardAccountService.loadById(record.getAccountId()).orElse(null),
                AccountParameterKey.MOST_RECENTLY_CSV_RECORD_IMPORTED,
                record.getSourceRecordHash()
            )
        );

        return result;
    }

}
