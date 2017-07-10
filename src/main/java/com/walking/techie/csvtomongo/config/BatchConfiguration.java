package com.walking.techie.csvtomongo.config;

import com.walking.techie.csvtomongo.model.Domain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;

@Slf4j
@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

  @Autowired
  private MongoTemplate mongoTemplate;

  @Bean(destroyMethod = "")
  @StepScope
  public FlatFileItemReader<Domain> genericReader(
      @Value("#{stepExecutionContext['fileName']}") String fileName,
      @Value("#{stepExecutionContext['delimiter']}") String delimiter) {
    log.info("fileName:{}", fileName);
    log.info("delimiter:{}", delimiter);
    FlatFileItemReader<Domain> reader = new FlatFileItemReader<Domain>();
    reader.setResource(new ClassPathResource(fileName));
    reader.setLineMapper(new DefaultLineMapper<Domain>() {{
      setLineTokenizer(new DelimitedLineTokenizer() {{
        setNames(new String[]{"id", "name"});
        setDelimiter(delimiter);
      }});
      setFieldSetMapper(new BeanWrapperFieldSetMapper<Domain>() {{
        setTargetType(Domain.class);
      }});
    }});
    return reader;
  }


  @Bean
  public MongoItemWriter<Domain> writer() {
    MongoItemWriter<Domain> writer = new MongoItemWriter<Domain>();
    writer.setTemplate(mongoTemplate);
    writer.setCollection("domain");
    return writer;
  }
}
