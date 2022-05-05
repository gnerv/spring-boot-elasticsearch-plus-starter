package com.gnerv.plus.elasticsearch.client;

import com.gnerv.plus.elasticsearch.config.DatasourceProperties;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ligen
 * @since 2022/4/18 09:39
 */
public class RestHighLevelClientUtils {

    public static RestHighLevelClient restHighLevelClient(DatasourceProperties datasource) {
        RestHighLevelClient restHighLevelClient = null;
        RestClientBuilder restClientBuilder = null;
        try {
            restClientBuilder = getRestClientBuilder(datasource);
        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        restHighLevelClient = new RestHighLevelClient(restClientBuilder);
        return restHighLevelClient;
    }

    private static RestClientBuilder getRestClientBuilder(DatasourceProperties datasource) throws KeyStoreException,
            NoSuchAlgorithmException {
        List<HttpHost> httpHosts = new ArrayList<>();
        List<String> hosts = datasource.getHosts();
        hosts.forEach(node -> {
            try {
                String[] parts = StringUtils.split(node, ":");
                Assert.notNull(parts, "Must defined");
                Assert.state(parts.length == 2, "Must be defined as 'host:port'");
                httpHosts.add(new HttpHost(parts[0], Integer.parseInt(parts[1]), datasource.getScheme()));
            } catch (Exception e) {
                throw new IllegalStateException("Invalid ES nodes " + "property '" + node + "'", e);
            }
        });
        // Callback used the basic credential auth
        RestClientBuilder builder = RestClient.builder(httpHosts.toArray(new HttpHost[0]));

        // Callback used the default {@link RequestConfig} being set to the {@link CloseableHttpClient}
        builder.setRequestConfigCallback(requestConfigBuilder -> {
            requestConfigBuilder.setConnectTimeout(datasource.getConnectTimeout());
            requestConfigBuilder.setSocketTimeout(datasource.getSocketTimeout());
            requestConfigBuilder.setConnectionRequestTimeout(datasource.getConnectionRequestTimeout());
            return requestConfigBuilder;
        });
        SSLContextBuilder sslBuilder = SSLContexts.custom().loadTrustMaterial(new TrustAllStrategy());

        // Callback used the basic credential auth
        DatasourceProperties.Account account = datasource.getAccount();
        if (!StringUtils.isEmpty(account.getUsername()) && !StringUtils.isEmpty(account.getUsername())) {
            final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(account.getUsername(), account.getPassword()));
            builder.setHttpClientConfigCallback(httpAsyncClientBuilder -> {
                httpAsyncClientBuilder.setMaxConnTotal(datasource.getMaxConnectTotal());
                httpAsyncClientBuilder.setMaxConnPerRoute(datasource.getMaxConnectPerRoute());
                //这里可以设置一些参数，比如cookie存储、代理等等
                httpAsyncClientBuilder.disableAuthCaching();
                try {
                    httpAsyncClientBuilder.setSSLContext(sslBuilder.build());
                    httpAsyncClientBuilder.setSSLHostnameVerifier(new NoopHostnameVerifier());
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (KeyManagementException e) {
                    e.printStackTrace();
                }

                return httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            });
        }

        return builder;
    }

}
