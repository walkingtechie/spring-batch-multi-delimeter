package com.walking.techie.csvtomongo.jobs;

import com.walking.techie.csvtomongo.listener.PipeSeparatorStepListener;
import com.walking.techie.csvtomongo.model.Domain;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PipeSeparatedFileReaderJob {

  @Autowired
  private JobBuilderFactory jobBuilderFactory;
  @Autowired
  private StepBuilderFactory stepBuilderFactory;

  @Autowired
  private FlatFileItemReader<Domain> pipeSeparatorReader;

  @Autowired
  private MongoItemWriter<Domain> mongoItemWriter;

  @Bean
  public Job pipeDelimiterJob() {
    return jobBuilderFactory.get("pipeDelimiterJob").incrementer(new RunIdIncrementer())
        .start(pipeSeparatedStep())
        .build();
  }

  @Bean
  public Step pipeSeparatedStep() {
    return stepBuilderFactory.get("step1").<Domain, Domain>chunk(10).reader(pipeSeparatorReader)
        .writer(mongoItemWriter).listener(pipeStepListener()).build();
  }

  @Bean
  public PipeSeparatorStepListener pipeStepListener() {
    return new PipeSeparatorStepListener();
  }
}
