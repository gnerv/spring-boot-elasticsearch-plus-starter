package com.gnerv.plus.elasticsearch.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ligen
 * @since 2022/4/18 09:39
 */
@Slf4j
@Data
@ConfigurationProperties(prefix = "embracesource.elasticsearch-plus")
@ComponentScan(value = "com.gnerv")
public class ElasticsearchConfigProperties {

    private final static String DEFAULT = "default";

    private static Map<String, DatasourceProperties> DATASOURCE = new HashMap<>();

    static {
        DATASOURCE.put(DEFAULT, new DatasourceProperties());
    }

    public Map<String, DatasourceProperties> getDatasource() {
        return DATASOURCE;
    }

}
