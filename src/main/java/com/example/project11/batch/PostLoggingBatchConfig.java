package com.example.project11.batch;

import com.example.project11.domain.Post;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;


@Configuration
@RequiredArgsConstructor
@Slf4j
public class PostLoggingBatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final EntityManagerFactory entityManagerFactory;


    @Bean
    public Job loggingPostJob() {
        return new JobBuilder("loggingPostsJob", jobRepository)
                .start(loggingPostStep())
                .build();
    }

    @Bean
    public Step loggingPostStep() {
        return new StepBuilder("loggingPostStep", jobRepository)
                .<Post, String>chunk(100, transactionManager)
                .reader(postReader())
                .processor(postProcessor())
                .writer(postWriter())
                .build();
    }

    @Bean
    public JpaCursorItemReader<Post> postReader() {
        return new JpaCursorItemReaderBuilder<Post>()
                .name("postReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("Select p FROM Post p")
                .build();
    }

    @Bean
    public ItemProcessor<Post, String> postProcessor() {
        return post -> "제목 : " + post.getTitle() + "| 내용 : " + post.getContent();
    }

    @Bean
    public ItemWriter<String> postWriter() {
        return items -> items.forEach(System.out::println);
    }

}
