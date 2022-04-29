package com.embracesource.plus.elasticsearch.config;

import lombok.Data;

import java.util.List;

/**
 * @author ligen
 * @since 2022/4/18 09:39
 */
@Data
public class DatasourceProperties {

    private Boolean enable = true;

    /**
     * 连接地址 ip:port
     */
    private List<String> hosts;

    /**
     * 连接协议 http/https
     */
    private String scheme;

    /**
     * 连接超时时间(毫秒)
     */
    private Integer connectTimeout = 1000;

    /**
     * socket 超时时间
     */
    private Integer socketTimeout = 30000;

    /**
     * 连接请求超时时间
     */
    private Integer connectionRequestTimeout = 500;

    /**
     * 每个路由的最大连接数量
     */
    private Integer maxConnectPerRoute = 10;

    /**
     * 最大连接总数量
     */
    private Integer maxConnectTotal = 30;

    /**
     * 认证账户
     */
    private Account account = new Account();

    private Pool pool = new Pool();

    /**
     * 认证账户
     */
    @Data
    public static class Account {

        /**
         * 认证用户
         */
        private String username;

        /**
         * 认证密码
         */
        private String password;

    }

    @Data
    public static class Pool {
        private Integer maxTotal = 8;
        private Integer minIdle = 8;
        private Integer maxIdle = 0;
    }

}
