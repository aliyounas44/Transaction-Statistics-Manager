package com.n26.task.configurations;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Getter
@Setter
@ConfigurationProperties
@Component
public class Configurations {
    private long transactionStaleTimeSeconds;

    @PostConstruct
    public void init() {
        if(transactionStaleTimeSeconds < 1) {
            transactionStaleTimeSeconds = 60;
        }
    }
}
