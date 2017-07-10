package com.walking.techie.csvtomongo.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CommaSeparatorStepListener implements StepExecutionListener {

  @Override
  public void beforeStep(StepExecution stepExecution) {
    log.info("comma step listener called");
    stepExecution.getExecutionContext().putString("fileName", "comma-separated.csv");
    stepExecution.getExecutionContext().putString("delimiter", ",");
  }

  @Override
  public ExitStatus afterStep(StepExecution stepExecution) {
    return null;
  }
}
