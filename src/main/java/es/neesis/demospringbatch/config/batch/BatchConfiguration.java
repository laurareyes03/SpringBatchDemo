package es.neesis.demospringbatch.config.batch;

import es.neesis.demospringbatch.processor.UserEditProcessor;
import es.neesis.demospringbatch.tasklet.ShowUserInfoTasklet;
import es.neesis.demospringbatch.writer.UserUpdaterWriter;
import es.neesis.demospringbatch.writer.UserWriter;
import lombok.RequiredArgsConstructor;
import es.neesis.demospringbatch.dto.User;
import es.neesis.demospringbatch.listener.UserExecutionListener;
import es.neesis.demospringbatch.model.UserEntity;
import es.neesis.demospringbatch.processor.UserProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public ItemReader<User> reader() {
        return new FlatFileItemReaderBuilder<User>()
                .name("userItemReader")
                .resource(new ClassPathResource("sample.csv"))
                .linesToSkip(1)
                .delimited()
                .names("id", "username", "password", "email")
                .fieldSetMapper(new BeanWrapperFieldSetMapper<User>() {{
                    setTargetType(User.class);
                }}).build();
    }

    @Bean
    public ItemReader<User> readerBDD(DataSource dataSource){
        return new JdbcCursorItemReaderBuilder<User>()
                .name("readerBDD")
                .dataSource(dataSource)
                .sql("SELECT id, username, password, email FROM users ")
                .rowMapper(new BeanPropertyRowMapper<>(User.class))
                .build();
    }

    @Bean
    public UserProcessor processor() {
        return new UserProcessor();
    }

    @Bean
    public UserEditProcessor editProcessor() {
        return new UserEditProcessor();
    }


    @Bean
    public ItemWriter<UserEntity> writer(DataSource dataSource) {
        return new UserWriter(dataSource);
    }

    @Bean
    public ItemWriter<UserEntity> updateWriter(DataSource dataSource) {
        return new UserUpdaterWriter(dataSource);
    }

    @Bean
    public Job importUserJob(UserExecutionListener listener, Step step1, Step step2, Step step3) {
        return jobBuilderFactory.get("importUserJob")
                .listener(listener)
                .start(step1)
                .next(step2)
                .next(step3)
                .build();
    }

    @Bean
    public Step step1(ItemReader<User> reader, ItemWriter<UserEntity> writer, ItemProcessor<User, UserEntity> processor) {
        return stepBuilderFactory.get("step1")
                .<User, UserEntity>chunk(2) // El processor se ejecutará cada 2 registros de manera secuencial
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Step step2(ItemReader<User> readerBDD, ItemWriter<UserEntity> updateWriter, ItemProcessor<User, UserEntity> editProcessor) {
        return stepBuilderFactory.get("step2")
                .<User, UserEntity>chunk(2) // El processor se ejecutará cada 2 registros de manera secuencial
                .reader(readerBDD)
                .processor(editProcessor)
                .writer(updateWriter)
                .build();
    }

    @Bean
    public Step step3(ShowUserInfoTasklet showUserInfoTasklet) {
        return stepBuilderFactory.get("step3")
                .tasklet(showUserInfoTasklet)
                .build();
    }


}
