package io.geekmind.budgie.balance;

import io.geekmind.budgie.model.dto.BalanceCalculationRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BaseBalanceCalculationStepTest {

    @Test
    public void proceed_withNoChainedNextStepAndShouldExecuteReturningTrue_InvokesCalculateAndWontFail() {
        BaseBalanceCalculationStep step = spy(new StubBaseBalanceCalculationStep(null));
        doNothing().when(step).calculate(any(BalanceCalculationRequest.class));
        doReturn(Boolean.TRUE).when(step).shouldExecute(any(BalanceCalculationRequest.class));

        step.proceed(new BalanceCalculationRequest());

        verify(step).calculate(any(BalanceCalculationRequest.class));
    }

    @Test
    public void proceed_withChainedNextStepAndShouldExecuteReturningTrue_InvokesCalculateAndProceedFromNextStep() {
        BaseBalanceCalculationStep nextChainedStep = mock(BaseBalanceCalculationStep.class);
        doNothing().when(nextChainedStep).proceed(any(BalanceCalculationRequest.class));
        BaseBalanceCalculationStep step = spy(new StubBaseBalanceCalculationStep(nextChainedStep));
        doNothing().when(step).calculate(any(BalanceCalculationRequest.class));
        doReturn(Boolean.TRUE).when(step).shouldExecute(any(BalanceCalculationRequest.class));

        step.proceed(new BalanceCalculationRequest());

        verify(step).calculate(any(BalanceCalculationRequest.class));
        verify(nextChainedStep).proceed(any(BalanceCalculationRequest.class));
    }

    @Test
    public void proceed_withChainedNextStepAndShouldExecuteReturningFalse_DelegatesToNextStep() {
        BaseBalanceCalculationStep nextChainedStep = mock(BaseBalanceCalculationStep.class);
        doNothing().when(nextChainedStep).proceed(any(BalanceCalculationRequest.class));
        BaseBalanceCalculationStep step = spy(new StubBaseBalanceCalculationStep(nextChainedStep));
        doReturn(Boolean.FALSE).when(step).shouldExecute(any(BalanceCalculationRequest.class));

        step.proceed(new BalanceCalculationRequest());

        verify(step, never()).calculate(any(BalanceCalculationRequest.class));
        verify(nextChainedStep).proceed(any(BalanceCalculationRequest.class));
    }

}

class StubBaseBalanceCalculationStep extends BaseBalanceCalculationStep {

    public StubBaseBalanceCalculationStep(BaseBalanceCalculationStep nextChainedStep) {
        super(nextChainedStep);
    }

    @Override
    public void calculate(BalanceCalculationRequest balanceCalculationRequest) {
        throw new UnsupportedOperationException("This is a test intended stub.");
    }

    @Override
    public Boolean shouldExecute(BalanceCalculationRequest balanceCalculationRequest) {
        throw new UnsupportedOperationException("This is a test intended stub.");
    }
}