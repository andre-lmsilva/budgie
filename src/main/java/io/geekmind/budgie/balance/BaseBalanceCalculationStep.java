package io.geekmind.budgie.balance;

import io.geekmind.budgie.model.dto.balance.BalanceCalculationRequest;

import java.util.Optional;

/**
 * Defines the standard basic behavior to be honored by any step involved to calculate the balance for an account and
 * period.
 *
 * @author Andre Silva
 */
public abstract class BaseBalanceCalculationStep {

    private final Optional<BaseBalanceCalculationStep> nextChainedStep;

    /**
     * Default class constructor. Expects the next step in the chain to be invoked after the return of the method {@link
     * #calculate(BalanceCalculationRequest)}.
     *
     * @param nextChainedStep   Instance of the next step in the chain.
     */
    public BaseBalanceCalculationStep(BaseBalanceCalculationStep nextChainedStep) {
        this.nextChainedStep = Optional.ofNullable(nextChainedStep);
    }

    /**
     * Invokes the {@link #calculate(BalanceCalculationRequest)} method and delegates the result to be handled by the next chained step
     * when available.
     *
     * @param balanceCalculationRequest Instance of the balance calculation request being processed.
     */
    public void proceed(BalanceCalculationRequest balanceCalculationRequest) {
        if (this.shouldExecute(balanceCalculationRequest)) {
            this.calculate(balanceCalculationRequest);
        }
        this.nextChainedStep.ifPresent(step -> step.proceed(balanceCalculationRequest));
    }

    /**
     * Performs the required processed by the step. The changes on the received quote will be performed by reference.
     * @param balanceCalculationRequest Instance of the balance calculation request being processed.
     */
    public abstract void calculate(BalanceCalculationRequest balanceCalculationRequest);

    /**
     * Checks if the basic conditions to perform the calculation are present in the balance.
     *
     * @param balanceCalculationRequest Balance calculation request being processed.
     * @return <i>true</i> when the step can proceed and perform the calculation. Otherwise, <i>false</i>.
     */
    public abstract Boolean shouldExecute(BalanceCalculationRequest balanceCalculationRequest);

}
