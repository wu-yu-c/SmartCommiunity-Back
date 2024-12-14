package com.example.SmartCommunity.config;

import com.zhipu.oapi.ClientV4;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@ConfigurationProperties(prefix = "ai")
@Data
public class ZhipuAiConfig {
    private String apiKey = "ec74fa5e0c14aa959c222ec65a0f4487.wFlCw52T9ih31EOl";

    @Bean
    public ClientV4 getClientV4() {
        return new ClientV4.Builder(apiKey)
                .networkConfig(30, 60, 60, 60, TimeUnit.SECONDS)
                .build();
    }
}
