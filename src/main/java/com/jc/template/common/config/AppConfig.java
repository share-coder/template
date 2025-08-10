package com.jc.template.common.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.util.TimeZone;
import java.util.concurrent.Executor;

@Configuration
public class AppConfig {

    @Value("${template.thread.core.pool.size}")
    private int threadCorePoolSize;

    @Value("${template.thread.max.pool.size}")
    private int threadMaxPoolSize;

    @Value("${template.thread.queue.capacity}")
    private int threadQueueCapacity;

    @Value("${template.http.client.connection.timeout}")
    private int httpClientConnectionTimeout;

    @Value("${template.http.client.read.timeout}")
    private int httpClientReadTimeout;

    @Value("${template.time.zone}")
    String appTimeZone = "UTC";

    @PostConstruct
    public void init() {
        // Setting Spring Boot SetTimeZone
        TimeZone.setDefault(TimeZone.getTimeZone(appTimeZone));
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(threadCorePoolSize);
        executor.setMaxPoolSize(threadMaxPoolSize);
        executor.setQueueCapacity(threadQueueCapacity);
        executor.setThreadNamePrefix("TemplateAsync-");
        executor.initialize();
        return executor;
    }

    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(httpClientConnectionTimeout);
        factory.setReadTimeout(httpClientReadTimeout);
        return new RestTemplate(factory);
    }
}
