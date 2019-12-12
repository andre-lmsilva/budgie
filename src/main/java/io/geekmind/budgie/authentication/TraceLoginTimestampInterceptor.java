package io.geekmind.budgie.authentication;

import io.geekmind.budgie.model.dto.account_parameter.ExistingAccountParameter;
import io.geekmind.budgie.model.entity.AccountParameterKey;
import io.geekmind.budgie.repository.AccountParameterService;
import io.geekmind.budgie.repository.StandardAccountService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Aspect
@Component
public class TraceLoginTimestampInterceptor {

    private final StandardAccountService standardAccountService;
    private final AccountParameterService accountParameterService;
    private final DateTimeFormatter dateTimeFormatter;

    public TraceLoginTimestampInterceptor(StandardAccountService standardAccountService,
                                          AccountParameterService accountParameterService) {
        this.standardAccountService = standardAccountService;
        this.accountParameterService = accountParameterService;
        this.dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    }

    @Pointcut("execution(* io.geekmind.budgie.authentication.DefaultAuthenticationProvider.authenticate(..))")
    private void authenticationInterceptor() { }

    @Around("authenticationInterceptor()")
    public Object interceptAuthentication(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Authentication authentication = (Authentication)proceedingJoinPoint.proceed();
        if (null != authentication) {
            this.updateLoginTimestamps();
        }
        return authentication;
    }

    @Transactional
    private void updateLoginTimestamps() {
       this.standardAccountService.getMainAccount()
           .ifPresent(mainAccount -> {
              this.accountParameterService.loadByAccountAndKey(mainAccount, AccountParameterKey.CURRENT_LOGIN_TIMESTAMP)
                  .ifPresent(parameter -> this.accountParameterService.upsert(mainAccount, AccountParameterKey.LAST_LOGIN_TIMESTAMP, parameter.getValue()));

              this.accountParameterService.upsert(
                  mainAccount,
                  AccountParameterKey.CURRENT_LOGIN_TIMESTAMP,
                  LocalDateTime.now().format(this.dateTimeFormatter)
              );
           });
    }

}
