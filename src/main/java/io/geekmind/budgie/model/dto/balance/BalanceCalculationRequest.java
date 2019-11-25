package io.geekmind.budgie.model.dto.balance;

import io.geekmind.budgie.model.dto.standard_account.ExistingStandardAccount;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Holds all the details handled along the balance calculation process.
 *
 * @author Andre Silva
 */
@Data
public class BalanceCalculationRequest {

    private Balance balance;

    private Map<ExistingStandardAccount, Balance> dependantAccountBalances;

    public static class BalanceCalculationRequestBuilder {

        private ExistingStandardAccount existingAccount;
        private LocalDate referenceDate;
        private Map<ExistingStandardAccount, Balance> dependantAccountBalances;
        private BalanceType balanceType = BalanceType.REGULAR_PERIOD_BALANCE;

        public BalanceCalculationRequestBuilder withBalanceType(BalanceType balanceType) {
            this.balanceType = balanceType;
            return this;
        }

        public BalanceCalculationRequestBuilder withAccount(ExistingStandardAccount existingAccount) {
            this.existingAccount = existingAccount;
            return this;
        }

        public BalanceCalculationRequestBuilder withReferenceDate(LocalDate referenceDate) {
            this.referenceDate = referenceDate;
            return this;
        }

        public BalanceCalculationRequestBuilder addDependantAccountBalance(Balance balance) {
            if (null == this.dependantAccountBalances) {
                this.dependantAccountBalances = new HashMap<>();
            }
            this.dependantAccountBalances.put(balance.getAccount(), balance);
            return this;
        }

        public BalanceCalculationRequest build() {
            BalanceCalculationRequest request = new BalanceCalculationRequest();
            request.setDependantAccountBalances(this.dependantAccountBalances);

            Balance balance = new Balance();
            request.setBalance(balance);
            balance.setAccount(this.existingAccount);
            balance.setBalanceType(this.balanceType);

            BalanceDates balanceDates = new BalanceDates();
            balance.setBalanceDates(balanceDates);
            balanceDates.setReferenceDate(Optional.ofNullable(this.referenceDate).orElse(LocalDate.now()));
            return request;
        }

    }

}
