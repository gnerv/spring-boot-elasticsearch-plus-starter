package com.gnerv.plus.elasticsearch.client;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.aop.support.AopUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * @author ligen
 * @since 2022/4/18 09:39
 */
@Slf4j
@AllArgsConstructor
public class ElasticsearchClient {

    private final ElasticsearchClientPool elasticsearchClientPool;

    @SneakyThrows
    public RestHighLevelClient getRestHighLevelClient() {
        return elasticsearchClientPool.getRestHighLevelClient();
    }

    @SneakyThrows
    public RestHighLevelClient getRestHighLevelClient(String name) {
        return elasticsearchClientPool.getRestHighLevelClient(name);
    }

}
