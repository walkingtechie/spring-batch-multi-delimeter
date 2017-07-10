package com.walking.techie.csvtomongo.jobs;

import com.walking.techie.csvtomongo.listener.CommaSeparatorStepListener;
import com.walking.techie.csvtomongo.model.Domain;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableBatchProcessing
@Configuration
public class CommaSeparatedFileReaderJob {

  @Autowired
  private JobBuilderFactory jobBuilderFactory;
  @Autowired
  private StepBuilderFactory stepBuilderFactory;

  @Autowired
  private FlatFileItemReader<Domain> commaSeparatorReader;

  @Autowired
  private MongoItemWriter<Domain> mongoItemWriter;

  @Bean
  public Job commaDelimiterJob() {
    return jobBuilderFactory.get("commaDelimiterJob").incrementer(new RunIdIncrementer())
        .start(commaSeparatedStep()).build();
  }

  @Bean
  public Step commaSeparatedStep() {
    return stepBuilderFactory.get("commaSeparatedStep").<Domain, Domain>chunk(10)
        .reader(commaSeparatorReader).writer(mongoItemWriter).listener(commaStepListener()).build();
  }

  @Bean
  public CommaSeparatorStepListener commaStepListener() {
    return new CommaSeparatorStepListener();
  }
}
