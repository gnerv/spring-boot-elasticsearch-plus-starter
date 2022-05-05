package com.gnerv.plus.elasticsearch.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * @author ligen
 * @since 2022/4/18 09:39
 */
@Data
@ConfigurationProperties(prefix = "embracesource.elasticsearch-plus")
public class ElasticsearchConfigProperties {

    private Map<String, DatasourceProperties> datasource;

}
