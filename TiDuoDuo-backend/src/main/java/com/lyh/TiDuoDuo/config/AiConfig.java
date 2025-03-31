package com.lyh.TiDuoDuo.config;

import com.zhipu.oapi.ClientV4;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href=https://github.com/fearlesslyh> 梁懿豪 </a>
 * @version 1.0
 * @date 2025/3/31 16:15
 */
@Configuration
@ConfigurationProperties(prefix = "ai")
@Data
public class AiConfig {
    /**
     * api key
     */
    private String apiKey;
    @Bean
    public ClientV4 getClientV4Obj(){
       return new ClientV4.Builder(apiKey).build();
    }
}
