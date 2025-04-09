package es.neesis.demospringbatch.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserStepInicialExecutionListener implements StepExecutionListener {
    @Override
    public void beforeStep(StepExecution stepExecution) {
        log.info("Step con id {} inserta a BD users por defecto", stepExecution.getStepName());

    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.info("Ya insertados los users por defecto");
        log.info("Estado del Step: {}", stepExecution.getStatus());

        return stepExecution.getExitStatus();
    }
}
