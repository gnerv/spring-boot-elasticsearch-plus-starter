package com.embracesource.plus.elasticsearch.config;

import com.embracesource.plus.elasticsearch.client.ElasticsearchClientPool;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ligen
 * @since 2022/4/18 09:39
 */
@Configuration
@EnableConfigurationProperties(ElasticsearchConfigProperties.class)
public class ElasticsearchAutoConfiguration {

    private final ElasticsearchConfigProperties elasticsearchConfigProperties;

    public ElasticsearchAutoConfiguration(ElasticsearchConfigProperties elasticsearchConfigProperties) {
        this.elasticsearchConfigProperties = elasticsearchConfigProperties;
    }

    @Bean
    public ElasticsearchClientPool elasticsearchClientPool(){
        return new ElasticsearchClientPool(this.elasticsearchConfigProperties);
    }

}
