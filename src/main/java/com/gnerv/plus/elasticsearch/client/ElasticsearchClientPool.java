package com.gnerv.plus.elasticsearch.client;

import com.gnerv.plus.elasticsearch.config.DatasourceProperties;
import com.gnerv.plus.elasticsearch.config.ElasticsearchConfigProperties;
import lombok.SneakyThrows;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ligen
 * @since 2022/4/18 09:39
 */
public class ElasticsearchClientPool {

    private static Map<String, GenericObjectPool<RestHighLevelClient>> REST_HIGH_LEVEL_CLIENT = new HashMap<>();

    public ElasticsearchClientPool(ElasticsearchConfigProperties elasticsearchConfigProperties) {
        handlerElasticsearchConfig(elasticsearchConfigProperties);
    }

    @SneakyThrows
    public RestHighLevelClient getRestHighLevelClient() {
        for (String string : REST_HIGH_LEVEL_CLIENT.keySet()) {
            return REST_HIGH_LEVEL_CLIENT.get(string).borrowObject();
        }
        return null;
    }

    @SneakyThrows
    public RestHighLevelClient getRestHighLevelClient(String name) {
        GenericObjectPool<RestHighLevelClient> restHighLevelClientGenericObjectPool = REST_HIGH_LEVEL_CLIENT.get(name);
        if (ObjectUtils.isEmpty(restHighLevelClientGenericObjectPool)) {
            return null;
        }
        return REST_HIGH_LEVEL_CLIENT.get(name).borrowObject();
    }

    private void handlerElasticsearchConfig(ElasticsearchConfigProperties elasticsearchConfigProperties) {
        Map<String, DatasourceProperties> datasource = elasticsearchConfigProperties.getDatasource();
        for (Map.Entry<String, DatasourceProperties> stringDatasourcePropertiesEntry : datasource.entrySet()) {
            createPool(stringDatasourcePropertiesEntry.getKey(), stringDatasourcePropertiesEntry.getValue());
        }
    }

    private void createPool(String name, DatasourceProperties datasourceProperties) {
        GenericObjectPoolConfig genericObjectPoolConfig = genericObjectPoolConfig(datasourceProperties);
        PooledObjectFactory<RestHighLevelClient> restHighLevelClientPooledObjectFactory =
                pooledObjectFactory(datasourceProperties);
        REST_HIGH_LEVEL_CLIENT.put(name, new GenericObjectPool<>(restHighLevelClientPooledObjectFactory,
                genericObjectPoolConfig));
    }

    private GenericObjectPoolConfig genericObjectPoolConfig(DatasourceProperties datasourceProperties) {
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMinIdle(datasourceProperties.getPool().getMinIdle());
        poolConfig.setMaxIdle(datasourceProperties.getPool().getMaxIdle());
        poolConfig.setMaxTotal(datasourceProperties.getPool().getMaxTotal());
        poolConfig.setJmxEnabled(false);
        return poolConfig;
    }

    private PooledObjectFactory<RestHighLevelClient> pooledObjectFactory(DatasourceProperties datasourceProperties) {
        return new BasePooledObjectFactory<RestHighLevelClient>() {

            @Override
            public void destroyObject(PooledObject<RestHighLevelClient> pooledObject) throws Exception {
                RestHighLevelClient highLevelClient = pooledObject.getObject();
                highLevelClient.close();
            }

            @Override
            public RestHighLevelClient create() throws Exception {
                RestHighLevelClient restHighLevelClient =
                        RestHighLevelClientUtils.restHighLevelClient(datasourceProperties);
                return restHighLevelClient;
            }

            @Override
            public PooledObject<RestHighLevelClient> wrap(RestHighLevelClient restHighLevelClient) {
                return new DefaultPooledObject<>(restHighLevelClient);
            }
        };
    }


}
